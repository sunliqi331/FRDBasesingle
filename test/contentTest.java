import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class contentTest {

	public static void main(String[] args) {
		Writer out = null;
		try {

			File outFile = new File("E://20180129.txt");
			if(outFile.exists()) {
				outFile.delete();
			} else {
				outFile.createNewFile();
			}
			out = new FileWriter(outFile);
			for (int j = 0; j < 50; j++) {
				for (int i = 0; i < 10000; i++) {
					out.write(getIntegerStringFormat(18, i) + ",测试用户" + j);
					out.write("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	        try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    public static String getIntegerStringFormat(int count, int number) {
        return String.format("%0" + count + "d", number);
    }
	

}
