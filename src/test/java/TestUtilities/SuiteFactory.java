//package TestUtilities;
//
//import org.testng.annotations.Factory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SuiteFactory {
//
//    @Factory
//    public Object[] createInstances() {
//        int numbersCount = 3; // مؤقت، أو هات القيمة من داتا ثابتة بدل driver
//        List<Object> allTests = new ArrayList<>();
//
//        for (int i = 0; i < numbersCount; i++) {
//            allTests.add(new TestCases.FullFlowTest(i));
//        }
//
//        return allTests.toArray();
//    }
//}
