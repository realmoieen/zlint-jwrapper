import com.moieen.zlint.jwrapper.LintResult;
import com.moieen.zlint.jwrapper.ZLintException;
import com.moieen.zlint.jwrapper.ZLinter;

public class ZLinterExample {

    public static void main(String[] args) throws ZLintException {
        //first set the ZLint path
        ZLinter.setZlintPath("Directory_Containing_Zlint");

        //Lint certificate,crl
        LintResult lintResult = ZLinter.lint("certificate.cer", ZLinter.Format.pem);

        //validate result
        System.out.println(lintResult.isPassed());
    }
}
