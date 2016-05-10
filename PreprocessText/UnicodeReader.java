package preprocessTxt;
//from:http://akini.mbnet.fi/java/unicodereader/UnicodeReader.java.txt
import java.io.*;
import java.io.Reader;

public class UnicodeReader extends Reader {
//解决java读取utf-8文件的乱码问题
	PushbackInputStream internalIn;
	InputStreamReader internalIn2 = null;
	String defaultEnc;
	
	private static final int BOM_SIZE =  4;
	
	public UnicodeReader(InputStream in,String defaultEnc) {
		internalIn = new PushbackInputStream(in, BOM_SIZE);
		this.defaultEnc = defaultEnc;
		// TODO Auto-generated constructor stub
	}
	
	public String getDefaultEncoding(){
		return defaultEnc;
	}
	
	public String getEncoding(){
		if(internalIn2 == null)
			return null;
		return internalIn2.getEncoding();
	}
	
	protected void init() throws IOException {
		if(internalIn2!=null) return;
		int n,unread;
		String encoding;
		byte bom [] = new byte[BOM_SIZE];
		n = internalIn.read(bom, 0, bom.length);
		
		if((bom[0] == (byte)0x00)&&(bom[1]==(byte)0x00)&&(bom[2]==(byte)0xFE)&&(bom[3]==(byte)0xFF)){
			encoding = "UTF-32BE";
			unread = n-4;
		}
		else if((bom[0] == (byte)0xFF)&&(bom[1]==(byte)0xFE)&&(bom[2]==(byte)0x00)&&(bom[3]==(byte)0x00)){
			encoding = "UTF-32LE";
			unread = n-4;
		}
		else if((bom[0] == (byte)0xEF)&&(bom[1]==(byte)0xBB)&&(bom[2]==(byte)0xBF)){
			encoding = "utf-8";
			unread = n-3;
		}
		else if((bom[0]==(byte)0xFE)&&(bom[1]==(byte)0xFF)){
			encoding = "UTF-16BE";
			unread = n-2;
		}
		else if((bom[0]==(byte)0xFF)&&(bom[1]==(byte)0xFE)){
			encoding = "UTF-16LE";
			unread = n-2;
		}
		else{
			encoding = defaultEnc;
			unread = n;
		}
		if(unread>0){
			internalIn.unread(bom, n-unread, unread);
		}
		if(encoding == null){
			internalIn2 = new InputStreamReader(internalIn);
		}
		else{
			internalIn2 = new InputStreamReader(internalIn,encoding); 
		}
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		init();
		return internalIn2.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		init();
		internalIn2.close();
	}

}
