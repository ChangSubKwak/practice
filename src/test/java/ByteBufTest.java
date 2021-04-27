import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ByteBufTest {
	@Test
	@DisplayName("ByteBuf 고정 버퍼 테스트")
	public void fixedBufferTest() {
		// given, when #1
		ByteBuf buf = Unpooled.buffer(11);
		System.out.println("1st State : " + buf);

		// then #1
		assertThat(buf.capacity()).isEqualTo(11);
		assertThat(buf.isDirect()).isFalse();

		// when #2
		buf.writeInt(0x12345678);

		// then #2
		assertThat(buf.readableBytes()).isEqualTo(4);
		assertThat(buf.writableBytes()).isEqualTo(7);

		assertThat(buf.readShort()).isEqualTo((short) 0x1234);
		assertThat(buf.readableBytes()).isEqualTo(2);
		assertThat(buf.writableBytes()).isEqualTo(7);
		assertThat(buf.isReadable()).isTrue();
		assertThat(buf.readShort()).isEqualTo((short) 0x5678);
		assertThat(buf.isReadable()).isFalse();

		// when #3
		buf.clear();

		// then #3
		assertThat(buf.readableBytes()).isEqualTo(0);
		assertThat(buf.writableBytes()).isEqualTo(11);
	}

	@Test
	@DisplayName("ByteBuf 동적 버퍼 테스트")
	public void dynamicBufferTest() {
		// given, when #1
		ByteBuf buf = Unpooled.buffer(11);
		System.out.println("1st State : " + buf);

		// then #1
		assertThat(buf.capacity()).isEqualTo(11);
		assertThat(buf.isDirect()).isFalse();

		// when #2
		buf.writeBytes("Hello World".getBytes(Charset.defaultCharset()));
		System.out.println("2nd : " + buf);

		// then #2
		assertThat(buf.readableBytes()).isEqualTo(11);
		assertThat(buf.writableBytes()).isEqualTo(0);
		assertThat(buf.toString(Charset.defaultCharset())).isEqualTo("Hello World");

		// when #3
		buf.capacity(6);
		System.out.println("3rd : " + buf);

		// then #3
		assertThat(buf.toString(Charset.defaultCharset())).isEqualTo("Hello ");

		// when #4
		buf.capacity(13);
		System.out.println("4th : " + buf);

		// then #4
		assertThat(buf.toString(Charset.defaultCharset())).isEqualTo("Hello ");

		// when #5
		buf.writeBytes("World".getBytes());
		System.out.println("5th : " + buf);

		// then #5
		assertThat(buf.toString(Charset.defaultCharset())).isEqualTo("Hello World");
		assertThat(buf.capacity()).isEqualTo(13);
		assertThat(buf.writableBytes()).isEqualTo(2);

		// when #6
//		buf.writeBytes("Hello World Add".getBytes());
//		System.out.println(buf);
	}

	@Test
	@DisplayName("PooledHeapByteBuf 함수 및 예외 테스트")
	public void pooledHeapByteBufTest() {
		// given, when #1
		ByteBuf buf1 = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
		ByteBuf buf2 = buf1;

		// then #1
		assertThat(buf1.capacity()).isEqualTo(11);
		assertThat(buf1.isDirect()).isFalse();
		assertThat(buf2.capacity()).isEqualTo(11);
		assertThat(buf2.isDirect()).isFalse();
		assertThat(buf1).isEqualTo(buf2);
		assertThat(buf1.refCnt()).isEqualTo(buf2.refCnt());

		// when #2
		buf1.release();

		// then #2
		assertThatThrownBy(() -> {                // then
			buf1.release();
		}).isInstanceOf(IllegalReferenceCountException.class).hasMessageContaining("refCnt: " + buf1.refCnt() + ", decrement: 1");
	}
}
