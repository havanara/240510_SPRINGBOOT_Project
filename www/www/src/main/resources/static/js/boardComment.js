console.log("board comment js in")

document.getElementById('cmtAddBtn').addEventListener('click', ()=>{
    const cmtWriter = document.getElementById('cmtWriter').innerText;
    const cmtText = document.getElementById('cmtText').value;

    if(cmtText == null || cmtText == ''){
        alert("댓글을 입력해주세요.");
        document.getElementById('cmtText').focus();
        return false;
    }else{
        let cmtData = {
            bno : bnoVal,
            writer : cmtWriter,
            content : cmtText
        }

        console.log(cmtData);

        postComment(cmtData).then(result =>{
            if(result == 1){
                alert("댓글이 등록되었습니다.");
            }
            spreadCommentList(bnoVal);
        });
    }
});

async function postComment(cmtData){
    try {
        const url = "/comment/post"
        const config = {
            method : 'post',
            headers : {
                'content-type' : 'application/json; charset=utf-8'
            },
            body : JSON.stringify(cmtData)
        };

        const resp = await fetch(url, config);
        const result = await resp.text();
        return result; //위에서 result를 받기 위해서 리턴처리
    } catch (error) {
        console.log(error);
    }
}

async function getCommentListFromServer(bno, page){
    try {
        const resp = await fetch("/comment/list/"+bno+"/"+page);
        const result = await resp.json();
        return result;
    } catch (error) {
        console.log(error);
    }
}

//페이징 처리를 하여 한페이지(더보기)에 5개 댓글을 출력
//전체 게시글 수에 따른 페이지 수
function spreadCommentList(bno, page=1){ //들어오는 page의 매개변수가 없으면 에러를 내지말고 1로 처리해줘
    getCommentListFromServer(bno, page).then(result => {
        console.log(result); //ph cmtList
        //댓글 뿌리기
        const ul = document.getElementById('cmtListArea');
        if(result.cmtList.length > 0){
            if(page == 1){ //1page에서만 댓글 내용 지우기
                ul.innerHTML = ''; //ul에 원래 있던 html 값 지우기
            }
            for(let cvo of result.cmtList){
                let add = `<ul class="list-group list-group-flush" id="cmtListArea">`;
                add += `<li class="list-group-item" data-cno="${cvo.cno}">`;
                add += `<div class="mb-3"> no. ${cvo.cno}`;
                add += `<div class="fw-bold">${cvo.writer }</div>`;
                add += `${cvo.content }`;
                add += `</div>`;
                add += `<span class="badge rounded-pill text-bg-warning">${cvo.regDate}</span>`;
                //수정, 삭제 버튼
                add += `<button type="button" class="btn btn-outline-warning btn-sm mod" data-bs-toggle="modal" data-bs-target="#myModal">수정</button>`;
                add += `<button type="button" class="btn btn-outline-danger btn-sm del">삭제</button>`;
                add += `</li> </ul>`;
                ul.innerHTML += add;
            }
            //더보기 버튼 코드
            let moreBtn = document.getElementById('moreBtn');
            console.log(moreBtn);
            
            //moreBtn이 표시되는 조건
            //ex)pgvo.pageNo = 1 / realEndPage = 3
            //realEndPage보다 현재 내 페이지가 작으면 표시
            if(result.pgvo.pageNo < result.realEndPage){
                // style = "visivility:hidden"(숨김) -> visivility:visible(표시)
                moreBtn.style.visibility = 'visible'; //버튼 표시
                moreBtn.dataset.page = page+1; //1페이지 늘림
            }else{
                moreBtn.style.visibility = 'hidden'; //pgvo.pageNo > realEndPage 이라면 숨김
            }

        }else{
            ul.innerHTML = `<li class="list-group-item">댓글이 없습니다.</li>`;
        }
    });
}

//더보기 버튼 작업
document.addEventListener('click',(e)=>{
    if(e.target.id == 'moreBtn'){
        let page = parseInt(e.target.dataset.page);
        spreadCommentList(bnoVal,page);
    }
    //수정 시 모달창을 통해 댓글 입력받기
    else if(e.target.classList.contains('mod')){
        //내가 수정 버튼을 누른 댓글의 li
        let li = e.target.closest('li');

        //writer를 찾아서 id="modWriter" 에 넣기
        let modWriter = li.querySelector('.fw-bold').innerText;
        document.getElementById('modWriter').innerText = modWriter;

        //nextSibling : 한 부모안에서 다음 형제를 찾기
        let cmtText = li.querySelector('.fw-bold').nextSibling;
        console.log(cmtText);
        document.getElementById('cmtTextMod').value = cmtText.nodeValue;

        //수정 -> cno dataset으로 달기 cno, content
        document.getElementById('cmtModBtn').setAttribute("data-cno", li.dataset.cno);
    }
    else if(e.target.id == 'cmtModBtn'){
        let cmtModData = {
            cno : e.target.dataset.cno,
            content : document.getElementById('cmtTextMod').value
        };
        console.log(cmtModData);

        //비동기로 보내기
        updateCommentToServer(cmtModData).then(result => {
            console.log(result);
            if(result == '1'){
                alert("댓글이 수정되었습니다.");
                //모달창 닫기
                document.querySelector(".btn-close").click();
            }else{
                alert("댓글 수정이 실패되었습니다.");
                document.querySelector('.btn-close').click();
            }
            //화면에 댓글 다시 뿌리기
            spreadCommentList(bnoVal);
        });   
    }
    //삭제
    else if(e.target.classList.contains('del')){
        // let cnoVal = e.target.dataset.cno;
        let li = e.target.closest('li');
        let cnoVal = li.dataset.cno;
        removeCommentFromServer(cnoVal).then(result =>{
            if(result == '1'){
                alert("댓글이 삭제되었습니다.");
                //화면에 뿌리기
                spreadCommentList(bnoVal);
    // bnoVal 는 detail.html에 연결한
    // <script type="text/javascript">
	// spreadCommentList(bnoVal);
    // </script>
            }
        });
    }
});

async function updateCommentToServer(cmtModData){
    try {
        const url = "/comment/modify";
        const config = {
            method : "put",
            headers : {
                'content-type' : 'application/json; charset=utf-8'
            },
            body : JSON.stringify(cmtModData)
        };
        const resp = await fetch(url,config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    } 
}

async function removeCommentFromServer(cnoVal){
    try {
        const url = "/comment/delete/"+cnoVal;
        const config = {
            method : "delete"
        };
        const resp = await fetch(url,config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}