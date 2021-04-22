import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {
	@Test
	public void splitTest() {
		String input = "1,2";							// given
		String[] t1 = input.split(",");				// when
		assertThat(t1).contains("1");					// then  #1
		assertThat(t1).containsExactly("1","2");		// then  #2
	}
}
