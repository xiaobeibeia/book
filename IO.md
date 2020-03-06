# IO

> 阻塞和非阻塞主要指的是访问 IO 的线程是否会阻塞（或者说是等待）
> 线程访问资源，该资源是否准备就绪的一种处理方式

## BIO(传统的IO)

BIO是同步阻塞式的IO，以流的方式处理数据（效率低）

Socket编程就是BIO，一个socket连接处理一个线程。当多个socket请求与服务端建立连接时，服务端不能提供相应数量的处理线程，没有分配到处理线程的连接自然就会阻塞或者是被拒绝了。

### 创建一个服务器端Serve类

```java
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            System.out.println("哈哈");
            //接受请求(阻塞)
            Socket socket = serverSocket.accept();
            System.out.println("阻塞1");
            //获取输入流（阻塞）
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("阻塞2");
            //获取输出流
            System.out.println(reader.readLine());
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("找我什么事？");
            socket.close();
        }

    }
}
```

### 创建一个客户端Clientr类

```java
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        System.out.println("请输入:");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(line);
        //获取输入流（阻塞）
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
        socket.close();
    }
}

```

> 测试1：只启动服务端，控制台只输出了“哈哈”，说明serverSocket.accept();是阻塞的
>
> 测试2：启动客户端，此时服务端输出了“阻塞1”，没有输出“阻塞2‘’。
>
> 测试3：在客户端输入“你在吗”，服务端输出“找我什么事？”说明socket.getInputStream()也是阻塞的

## NIO（同步非阻塞式IO）

NIO是对BIO的改进，它是同步非阻塞的IO，以块的方式处理数据（效率高）。NIO基于通道和缓冲区进行数据操作，数据总是从通道读取到缓冲区中，或者从缓冲区中写到通道中。Selector（选择器）用于监听多个通道的时间，（比如：连接请求，数据到达等），因此使用单个线程就可以监听多个客户端通道。


### NIO的三大核心

NIO的三大核心：Channel（通道）、Buffer（缓冲区）、Selector（选择器）

##### 文件IO（不支持非阻塞的操作）

#### 缓冲区（buffer）

> 实际就是一个容器，是一个特殊的数组，缓冲区内部内置了一些机制，能够跟踪和记录缓冲区的状态和变化。Channel提供从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由Buffer。

##### 在NIO中Buffer是一个顶层抽象父类，常用的子类有：

> - ByteBuffer
> - ShortBuffer
> - ······

##### ByteBuffer·····类中的一些常用方法，这里以ByteBuffer类为例

> public abstract ByteBuffer put(byte[] b)：存储字节数据到缓冲区
> public abstract byte[] get()：从缓冲区获得字节数据
> public final byte[] array()：把缓冲区数据转换成字节数组
> public static ByteBuffer allocate(int capacity)：设置缓冲区的初始容量
> public final Buffer flip()： 翻转缓冲区，重置位置到初始位置
>

#### 通道（Channel）

> 类似于 BIO 中的 stream，例如 FileInputStream 对象，用来建立到目标（文件，网络套接字，硬件设备等）的一个连接，但是需要注意：BIO 中的 stream 是单向的，例如 FileInputStream 对象只能进行读取数据的操作，而 NIO 中的通道(Channel)是双向的，既可以用来进行读操作，也可以用来进行写操作。

##### 常用的 Channel 类有：

> - FileChannel 用于文件的数据读写，
> - DatagramChannel 用于 UDP 的数据读写，
> - ServerSocketChannel 和 SocketChannel 用于 TCP 的数据读写。

##### 常用的方法，以FileChannel类为例

> public int read(ByteBuffer dst) ，从通道读取数据并放到缓冲区中
> public int write(ByteBuffer src) ，把缓冲区的数据写到通道中
> public long transferFrom(ReadableByteChannel src, long position, long count)，从目标通道中复制数据到当前通道（特别适合复制大文件）
> public long transferTo(long position, long count, WritableByteChannel target)，把数据从当前通道复制给目标通道（特别适合复制大文件）

#### demo

向文件中写入数据、从文件中读取数据、复制文件

**使用文件IO时，把缓冲区中的数据写到Channel通道之前一定要调用Buffer类的flip（）方法，翻转缓冲区**

```java
public class TestNIO {
    @Test
    //向文件中写数据
    public void test1() throws Exception {
        //创建文件输出流
        FileOutputStream fos = new FileOutputStream("basic.txt");
        //获取通道
        FileChannel channel = fos.getChannel();
        //获取缓冲数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        //往缓区写入字节数组
        String str = "hello nio";
        byteBuffer.put(str.getBytes());
        //翻转缓冲区
        byteBuffer.flip();
        //把缓冲区写到通道中
        channel.write(byteBuffer);
        //关闭流
        fos.close();
    }


    @Test
    //从文件中读数据
    public void test2() throws Exception {
        File file = new File("basic.txt");
        //创建文件输入流
        FileInputStream fis = new FileInputStream(file);
        //获取通道
        FileChannel channel = fis.getChannel();
        //获取缓区
        ByteBuffer  byteBuffer = ByteBuffer.allocate((int) file.length());
        //从通道中读取数据到缓冲区中
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        //关闭流
        fis.close();
    }


    @Test
    //复制文件
    public void test3() throws Exception {
        //创建文件输入流
        FileInputStream fis = new FileInputStream("basic.txt");
        //创建文件输出流
        FileOutputStream fos = new FileOutputStream("I:\\test\\test.txt");
        //获取两个通道
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
        //把fisChannel通道中的数据复制到fosChannel通道中
        fosChannel.transferFrom(fisChannel, 0, fisChannel.size());
        //关闭流
        fis.close();
        fos.close();
    }
}
```

## AIOs(异步非阻塞)

先由操作系统完成客户端的请求，再通知服务器去启动线程进行处理。



## 三种对比

### BIO

适用于连接较少，对服务器资源消耗很大，但是编程简单。是同步阻塞的。

举例：你到餐馆点餐，然后在那儿等着，什么也做不了，只要饭还没有好，就要必须等着

### NIO

使用于连接数量比较多且连接时间比较短的架构，比如聊天服务器，编程比较复杂。是同步非阻塞的

举例：你到餐馆点完餐，然后就可以去玩儿了，玩一会儿就回饭馆问一声，饭好了没。

### AIO

适用于连接数量多而且连接时间长的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂。是异步非阻塞的。

举例：饭馆打电话给你说，我们知道你的位置，待会儿给您送来，你安心的玩儿就可以了。类似于外卖。






参考博客https://blog.csdn.net/dhrtyuhrthfgdhtgfh/article/details/90271123