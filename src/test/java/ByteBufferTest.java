import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

public class ByteBufferTest {
	@Test
	@DisplayName("ByteBuffer rewind 함수 테스트")
	public void rewindTest() {
		// given
		ByteBuffer bb = ByteBuffer.allocate(11);
		System.out.println("1st State : " + bb);

		// when #1
		bb.put((byte) 1);
		bb.put((byte) 2);
		System.out.println("2nd State : " + bb);

		// then #1
		assertThat(bb.position()).isEqualTo(2);
		assertThat(bb.limit()).isEqualTo(11);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #2
		bb.rewind();
		System.out.println("3rd State : " + bb);

		// then #2
		assertThat(bb.position()).isEqualTo(0);
		assertThat(bb.limit()).isEqualTo(11);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #3
		bb.get();									// add position to one
		System.out.println("4th State : " + bb);

		// then #3
		assertThat(bb.position()).isEqualTo(1);
		assertThat(bb.limit()).isEqualTo(11);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #4
		bb.get(1);									// do nothing about position
													// get함수에 인자 유무에 따라 position 증가하게 한 건 루프문에서 활용을 위한 듯
		System.out.println("5th State : " + bb);

		// then #4
		assertThat(bb.position()).isEqualTo(1);
		assertThat(bb.limit()).isEqualTo(11);
		assertThat(bb.capacity()).isEqualTo(11);
	}

	@Test
	@DisplayName("ByteBuffer flip 함수 테스트")
	public void flipTest() {
		// given
		ByteBuffer bb = ByteBuffer.allocate(11);
		System.out.println("1st State : " + bb);

		// when #1
		bb.put((byte) 1);
		bb.put((byte) 2);
		System.out.println("2nd State : " + bb);

		// then #1
		assertThat(bb.position()).isEqualTo(2);
		assertThat(bb.limit()).isEqualTo(11);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #2
		int posBeforeFlip = bb.position();
		bb.flip();
		System.out.println("3rd State : " + bb);

		// then #2
		assertThat(bb.position()).isEqualTo(0);
		assertThat(bb.limit()).isEqualTo(posBeforeFlip);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #3
		bb.get();
		System.out.println("4th State : " + bb);

		// then #3
		assertThat(bb.position()).isEqualTo(1);
		assertThat(bb.limit()).isEqualTo(posBeforeFlip);
		assertThat(bb.capacity()).isEqualTo(11);

		// when #4
		bb.get(1);
		System.out.println("5th State : " + bb);

		// then #4
		assertThat(bb.position()).isEqualTo(1);
		assertThat(bb.limit()).isEqualTo(posBeforeFlip);
		assertThat(bb.capacity()).isEqualTo(11);
	}
}
