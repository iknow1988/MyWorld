import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
	public static void main(String[] args) {
		Server nioClient = new Server();
		SocketChannel socketChannel = nioClient
				.createServerSocketChannel(Integer.parseInt(args[0]));
		nioClient.sendFile(socketChannel, args[1]);

	}

	public SocketChannel createServerSocketChannel(int port) {

		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			socketChannel = serverSocketChannel.accept();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return socketChannel;
	}

	public void sendFile(SocketChannel socketChannel, String fileName) {
		RandomAccessFile aFile = null;
		try {
			File file = new File(fileName);
			aFile = new RandomAccessFile(file, "r");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);
			while (inChannel.read(buffer) > 0) {
				buffer.flip();
				socketChannel.write(buffer);
				buffer.clear();
			}
			socketChannel.close();
			aFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}