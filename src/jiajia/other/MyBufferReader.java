package jiajia.other;

import java.io.*;
public class  MyBufferReader
{
 private InputStreamReader r;
 public MyBufferReader(InputStreamReader r)
 {
  this.r = r;
 }
 //可以一次读一行数据的方法。
 public String myReadLine()throws IOException
 {
  //定义一个临时容器。原BufferReader封装的是字符数组。
  //定义一个StringBuilder容器。因为最终还是要将数据变成字符串。
  StringBuilder sb = new StringBuilder();
  int ch = 0;
  while((ch=r.read())!=-1)
  {
   if(ch=='\r')//如果读到\r就不存到StringBuilder里面，不存还要继续往下读
    continue;//continue后继续回去读下一个数据
   if(ch==']'){
	   sb.append((char)ch);
	   return sb.toString();//读到"]"时，停止读取返回string
   }
//   if(ch=='\n')
//	sb.append('\n');//读到回车符时，仍继续读取
   else
    sb.append((char)ch);//强转--->读到一个字符就得存到缓冲区
  }
  if(sb.length()!=0)//如果缓冲区不等于0，就代表缓冲区有数据
   return sb.toString();
  return null;//读到结尾返回空
 }
 public void myClose()throws IOException
 {
  r.close();
 }
}
 

class MyBufferedReaderDemo
{
 public static void main(String[] args) throws IOException
 {
  FileReader fr = new FileReader("buf.txt");
  MyBufferReader myBuf = new MyBufferReader(fr);
  String line = null;
  while((line=myBuf.myReadLine())!=null)
  {
   System.out.println(line);
  }
  myBuf.myClose();
 }
}