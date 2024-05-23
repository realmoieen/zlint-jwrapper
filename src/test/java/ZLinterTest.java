import com.moieen.zlint.jwrapper.LintResult;
import com.moieen.zlint.jwrapper.Source;
import com.moieen.zlint.jwrapper.ZLintException;
import com.moieen.zlint.jwrapper.ZLinter;
import org.junit.jupiter.api.*;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZLinterTest {

    @Order(1)
    @Test
    public void test_zlint_directory_not_set() {
        Assertions.assertThrows(ZLintException.class, () -> ZLinter.lint("www_sc_com.cer", ZLinter.Format.pem));
    }

    @Order(2)
    @Test
    public void test_lint_valid_certificate() throws ZLintException {
        //first set the ZLint path
        ZLinter.setZlintPath(new File(ZLinterTest.class.getResource("./zlint").getPath()).toString());

        //Lint certificate,crl
        LintResult lintResult = ZLinter.lint(new File(ZLinterTest.class.getResource("./www_sc_com.cer").getPath()).toString(), ZLinter.Format.pem);

        //validate result
        Assertions.assertTrue(lintResult.isPassed());
    }

    @Test
    @Order(3)
    public void test_list_available_lints_not_empty() throws ZLintException {
        Assertions.assertTrue(!ZLinter.getAvailableLints().isEmpty());
    }


    @Test
    @Order(4)
    public void test_list_includes_sources() throws ZLintException {
        //Lint certificate,crl
        LintResult lintResult = ZLinter.lint(new File(ZLinterTest.class.getResource("./www_sc_com.cer").getPath()).toString(), ZLinter.Format.pem, Source.Apple);

        //validate result
        Assertions.assertTrue(lintResult.getLints().stream().allMatch(lint -> lint.getSource().equals(Source.Apple)));
    }

}
