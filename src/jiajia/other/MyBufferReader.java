package jiajia.other;

import java.io.*;
public class  MyBufferReader
{
 private InputStreamReader r;
 public MyBufferReader(InputStreamReader r)
 {
  this.r = r;
 }
 //����һ�ζ�һ�����ݵķ�����
 public String myReadLine()throws IOException
 {
  //����һ����ʱ������ԭBufferReader��װ�����ַ����顣
  //����һ��StringBuilder��������Ϊ���ջ���Ҫ�����ݱ���ַ�����
  StringBuilder sb = new StringBuilder();
  int ch = 0;
  while((ch=r.read())!=-1)
  {
   if(ch=='\r')//�������\r�Ͳ��浽StringBuilder���棬���滹Ҫ�������¶�
    continue;//continue�������ȥ����һ������
   if(ch==']'){
	   sb.append((char)ch);
	   return sb.toString();//����"]"ʱ��ֹͣ��ȡ����string
   }
//   if(ch=='\n')
//	sb.append('\n');//�����س���ʱ���Լ�����ȡ
   else
    sb.append((char)ch);//ǿת--->����һ���ַ��͵ô浽������
  }
  if(sb.length()!=0)//���������������0���ʹ�������������
   return sb.toString();
  return null;//������β���ؿ�
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