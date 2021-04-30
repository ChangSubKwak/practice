import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

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

	@Test
	public void pooledHeapByteBufTest2() {
		// given
		ByteBuf buf = Unpooled.buffer();

		// when #1
		buf.writeShort(1);
		buf.markReaderIndex();

		// then #1
		assertThat(buf.readShort()).isEqualTo((short) 1);
		assertThat(buf.readerIndex()).isEqualTo(2);

		buf.resetReaderIndex();                                // when #2
		assertThat(buf.readerIndex()).isEqualTo(0);            // then #2

		assertThat(buf.readShortLE()).isEqualTo((short) 0x0100);       // when, then #3
	}

	@Test
	@DisplayName("자바의 ByteBuffer과 네티의 ByteBuf 사이의 상호 변환 테스트")
	public void convertNettyBufferTest() {
		// given
		String testStr = "Keep up the good work";
		ByteBuf nettyBuf = Unpooled.buffer(testStr.length());

		nettyBuf.writeBytes(testStr.getBytes());                                       // when #1
		assertThat(testStr).isEqualTo(nettyBuf.toString(Charset.defaultCharset()));    // then #1

		ByteBuffer nioBuf = nettyBuf.nioBuffer();
		assertThat(nioBuf).isNotNull();

		System.out.println(Arrays.toString(nioBuf.array()));
		System.out.println(nioBuf.arrayOffset());
		System.out.println(nioBuf.remaining());
	}

	@Test
	@DisplayName("자바의 ByteBuffer과 네티의 ByteBuf 사이의 Wrapping 함수를 통한 메모리 공유 확인 테스트")
	public void convertNettyBufferTest2() {
		// given
		String testStr = "Keep up the good work";

		// when #1
		ByteBuffer nioBuf = ByteBuffer.wrap(testStr.getBytes());
		ByteBuf nettyBuf = Unpooled.wrappedBuffer(nioBuf);

		// then #1
		assertThat(nioBuf.get(0)).isEqualTo(nettyBuf.getByte(0));

		nioBuf.put(0, (byte) ('K' + 1));                                    // when #2
		assertThat(nioBuf.get(0)).isEqualTo(nettyBuf.getByte(0));        // then #2
	}

	private ByteBuf a(ByteBuf input) {
		return input;
	}

	private ByteBuf b(ByteBuf input) {
		//ByteBuf output = null;					// ByteBuf를 별도로 선언하면 refCnt값이 증가함
		try {
			//output = input.alloc().directBuffer(input.readableBytes() + 1);
			//return output;
			return input;
		} finally {
			//input.release();
		}
	}

	private void c(ByteBuf input) {
		input.release();
	}

	@Test
	public void releaseTest() {
		ByteBuf buf = Unpooled.buffer(10);
		c(b(a(buf)));
		System.out.println(buf.refCnt());
	}
}
