import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {
	@Test
	public void splitTest1_1() {
		String input = "1,2";							// given
		String[] s = input.split(",");				// when
		assertThat(s).containsExactly("1","2");		// then
	}

	@Test
	public void splitTest1_2() {
		String input = "1";							// given
		String[] s = input.split(",");			// when
		assertThat(s).contains("1");				// then
	}

	@Test
	public void splitTest2() {
		String input = "(1,2)";										// given
		String s = input.substring(1, input.length() - 1);	// when
		assertThat(s).isEqualTo("1,2");								// then
	}

	@Test
	@DisplayName("String의 chrAt()메소드를 활용해 특정 위치의 문자를 가져오는 테스트")
	public void splitTest3_1() {
		String input = "abc";										// given
		//String s = input.substring(1, input.length() - 1);	// when
		//assertThat(s).isEqualTo("1,2");								// then
		assertThat
	}
}
