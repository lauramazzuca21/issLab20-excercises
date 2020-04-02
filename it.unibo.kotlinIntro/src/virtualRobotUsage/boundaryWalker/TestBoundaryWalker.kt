package virtualRobotUsage.boundaryWalker

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
 
class TestRobotboundary0 {
	
	val perimeterInRobotUnits = 26;
	
	@Before
	fun systemSetUp() {
		virtualRobotSupport.setRobotTarget( robotBoundary  )
	}
	
	@After
	fun terminate() {
		println("%%%  TestRobotboundary0 terminates ")
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWork() {

		assert(robotBoundary.getCoveredCells().size == 0)
		boundaryWalker.main()
		assert(robotBoundary.getCoveredCells().size == perimeterInRobotUnits)
	}

}