package github.chenxh.media.flv;

import java.io.File;
import java.io.IOException;

public class TestFixFlv {
    public static void main(String[] args) throws IOException {
        MetaDataFixer fixer = new MetaDataFixer();
        
        File source = new File(ITestFiles.ROOT_DIR, ITestFiles.FILE);
        File dest = new File(ITestFiles.ROOT_DIR, ITestFiles.NEW_FILE);
        System.out.println(dest.getAbsolutePath());

        fixer.fix(source, dest);

    }
}
