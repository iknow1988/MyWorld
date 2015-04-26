import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {

	public static void main(String[] args) {
		Client nioClient = new Client();
		SocketChannel socketChannel = nioClient.createChannel(args[0],
				Integer.parseInt(args[1]));
		nioClient.readFileFromSocket(socketChannel, args[2]);
	}

	public SocketChannel createChannel(String ip, int port) {

		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			socketChannel.connect(socketAddress);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return socketChannel;
	}

	public void readFileFromSocket(SocketChannel socketChannel, String fileName) {
		RandomAccessFile aFile = null;
		try {
			aFile = new RandomAccessFile(fileName,"rw");
			ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);
			FileChannel fileChannel = aFile.getChannel();
			while (socketChannel.read(buffer) > 0) {
				buffer.flip();
				fileChannel.write(buffer);
				buffer.clear();
			}
			fileChannel.close();
			System.out.println("End of file reached..Closing channel");
			socketChannel.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}