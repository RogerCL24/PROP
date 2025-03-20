
import algorism.VorazTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import types.PairTest;
import algorism.AproximationKruskalILSTest;


@RunWith(value = Suite.class)

@SuiteClasses(value={DistributionTest.class, AproximationKruskalILSTest.class, PairTest.class,MensajeTest.class,
CjtDistribucionesTest.class,CjtUsersTest.class,LlistaProductesTest.class,CjtLlistesProductesTest.class,UserTest.class, VorazTest.class})
public class MasterTestSuite {

}

