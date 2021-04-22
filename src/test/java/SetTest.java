import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SetTest {
	private Set<Integer> numbers;

	@BeforeEach
	public void setUp() {
		numbers = new HashSet<>();
		numbers.add(1);
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
	}

	@Test
	public void size() {
		assertThat(numbers.size()).isEqualTo(3);	// then
	}

	@Test
	public void contains1() {
		assertThat(numbers.contains(1)).isTrue();	// when - then #1
		assertThat(numbers.contains(2)).isTrue();	// when - then #2
		assertThat(numbers.contains(3)).isTrue();	// when - then #3
	}

	@Test
	public void contains2() {
		assertThat(numbers.contains(1)).isTrue();	// when - then #1
		assertThat(numbers.contains(2)).isTrue();	// when - then #2
		assertThat(numbers.contains(3)).isTrue();	// when - then #3
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3})
	public void isSetContains1(Integer i) {
		assertThat(numbers.contains(i)).isTrue();
	}

	@ParameterizedTest
	@CsvSource(value = { "1 : true", "2 : true", "3:true", "4:false", "5:false" }, delimiter = ':')
	public void isSetContains2(int i, boolean b) {
		assertThat(numbers.contains(i)).isEqualTo(b);
	}
}
