import com.moieen.zlint.jwrapper.LintResult;
import com.moieen.zlint.jwrapper.ZLintException;
import com.moieen.zlint.jwrapper.ZLinter;
import org.junit.jupiter.api.*;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZLinterTest {

    @Order(1)
    @Test
    public void test_zlint_directory_not_set() {
        Assertions.assertThrows(ZLintException.class, () -> ZLinter.lint("certificate.cer", ZLinter.Format.pem));
    }

    @Order(2)
    @Test
    public void testValid() {
        //first set the ZLint path
        ZLinter.setZlintPath(new File(ZLinterTest.class.getResource("./zlint").getPath()).toString());

        //Lint certificate,crl
        LintResult lintResult = null;
        try {
            lintResult = ZLinter.lint(new File(ZLinterTest.class.getResource("./www_sc_com.cer").getPath()).toString(), ZLinter.Format.pem);
        } catch (ZLintException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            Assertions.fail(e.getMessage());
        }

        //validate result
        Assertions.assertTrue(lintResult.isPassed());
    }
}
