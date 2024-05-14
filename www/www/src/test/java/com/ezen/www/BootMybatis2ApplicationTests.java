package com.ezen.www;

import com.ezen.www.domain.BoardVO;
import com.ezen.www.repository.BoardMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*@RunWith(SpringRunner.class)*/
@SpringBootTest(classes = BootMybatis2Application.class)
class BootMybatis2ApplicationTests {

	@Autowired
	private BoardMapper mapper;

	@Test
	void contextLoads() {
		for(int i=0; i<300; i++){ //페이징 하기 위한 게시글 작성 작업
			BoardVO bvo = BoardVO
					.builder() //BoardVO에 Builder 설정이 되어있어야 가능
					.title("Title "+i)
					.writer("tester@test.com")
					.content("Test Content "+i)
					.build();

			mapper.insert(bvo);
		}
	}

}
