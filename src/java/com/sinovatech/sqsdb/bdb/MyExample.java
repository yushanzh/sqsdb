package com.sinovatech.sqsdb.bdb;

import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.JEVersion;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;

public class MyExample {
	TestConfig testConfig;
	BdbWorkQueue  bq;

	public MyExample(String args[]) {
        testConfig = new TestConfig(args);
//        bq = new BdbWorkQueue(testConfig);
    }

    public static void main(String[] args) {
        MyExample example = new MyExample(args);
        example.execute();
    }
    
    /**
     * Print the usage.
     */
    public static void usage(String msg) {
        String usageStr;
        if (msg != null) {
            System.err.println(msg);
        }
        usageStr = "Usage: java MyExamplen"
                + " [-h ] [-preload] [-dirtyRead]"
                + " [-useTxns] [-syncCommit]"
                + " [-deferredWrite]n"
                + " [-numThreads ]n"
                + " [-itemsPerTxn ]n"
                + " [-cacheSize ]n"
                + " [-logFileSize ]n"
                + " [-numOperations ]n"
                + " [-operationType ]";
        System.err.println(usageStr);
    }

    public void execute() {
    	
    	try {

    	if (testConfig.operationType.equalsIgnoreCase("READ")) {
            /* Read */
            databaseGet(null);
        } else if (testConfig.operationType.
                equalsIgnoreCase("DELETE")) {
            /* Delete */
            databaseDelete(null);
        } else if (testConfig.operationType.
                equalsIgnoreCase("INSERT")) {
            /* Insert */
            databasePutNoOverwrite(null);
        } else if (testConfig.operationType.
                equalsIgnoreCase("READ,DELETE")) {
            /* Update */
        	databaseGet(null);
        } else {
            /* Cursor scan with dirty read. */
            scan();
        }
    	} catch (Exception e) {
            System.err.println("TestJEDB: " + e);
            e.printStackTrace();
            System.exit(1);
        }


    }

    private void databasePutNoOverwrite(Object txn)
    throws Exception {

//    BasicEntity entity = new BasicEntity(keyNum);
//    primaryIndex.putNoReturn((Transaction) txn, entity);
}

private void databaseGet(Object txn) throws Exception  {

	bq.poll();
    // System.out.println("Read entity = " + e);
}


private void scan()  {

//    int numRecords = 0;
//    CursorConfig curConf = new CursorConfig();
//
//    EntityCursor cursor =
//        primaryIndex.entities((Transaction) txn, curConf);
//    try {
//        for (BasicEntity e : cursor) {
//            numRecords++;
//        }
//    } finally {
//        cursor.close();
//    }
//
//
//    System.out.println("scan records=" + numRecords);
//    return numRecords;
}

private void databaseDelete(Object txn)
    throws Exception {

//    primaryIndex.delete((Transaction) txn, new CompositeKey(keyNum));
}

    
    
    
    
    
    
    /**
     * Parses and contains all test properties.
     */
    static class TestConfig {
    	/* Use dirty reads. */
        static boolean dirtyRead = false;
        /* Preload records into memory by doing a scan before the test. */
        static boolean preload = false;
        /* Use the deferred write mode to speed up. */
        static boolean deferredWrite = false;
        /* Use synchronous commit. */
        static boolean syncCommit = false;
        /* Use transactions. */
        static boolean useTxns = false;
        /* Number of threads to use. */
        static int numThreads = 1;
        /* Number of items accessed per txn. */
        static int itemsPerTxn = 50;
        /* Cache size, default is 100M. */
        static long cacheSize = (200 << 20);
        /* JE's Log file size, default is 10M. */
        static long logFileSize = (10 << 20);
        /* Environment home. */
        static String envHome = "E:";
        static String storeName = "test1";

        /* Operations per test phase. */
        // static int numOperations = (1 << 20); // default to 1 million.
        static int numOperations = 1000000; // default to 1 million.
        /* Operation types include: INSERT, READ, SCAN, DELETE and UPDATE */
        static String operationType = "READ";
        /* Save command-line input arguments. */
        static StringBuffer inputArgs = new StringBuffer();

        
        private TestConfig(String args[]) {
        	if (args.length < 2) {
                usage(null);
                System.exit(1);
            }

            try {
                /* Parse command-line input arguments. */
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i];
                    boolean moreArgs = i < args.length - 1;
                    if (arg.equals("-h") && moreArgs) {
                        envHome = args[++i];
                    } else if (arg.equals("-dirtyRead")) {
                        dirtyRead = true;
                    } else if (arg.equals("-preload")) {
                        preload = true;
                    } else if (arg.equals("-deferredWrite")) {
                        deferredWrite = true;
                    } else if (arg.equals("-syncCommit")) {
                        syncCommit = true;
                    } else if (arg.equals("-useTxns")) {
                        useTxns = true;
                    } else if (arg.equals("-numThreads") && moreArgs) {
                        numThreads = Integer.parseInt(args[++i]);
                    } else if (arg.equals("-itemsPerTxn") && moreArgs) {
                        itemsPerTxn = Integer.parseInt(args[++i]);
                    } else if (arg.equals("-cacheSize") && moreArgs) {
                        cacheSize = Long.parseLong(args[++i]);
                    } else if (arg.equals("-logFileSize") && moreArgs) {
                        logFileSize = Long.parseLong(args[++i]);
                    } else if (arg.equals("-numOperations") && moreArgs) {
                        numOperations = Integer.parseInt(args[++i]);
                    } else if (arg.equals("-operationType") && moreArgs) {
                        operationType = args[++i];
                    } else if (arg.equals("-help")) {
                        usage(null);
                        System.exit(0);
                    } else {
                        usage("Unknown arg: " + arg);
                        System.exit(1);
                    }
                }

                /* Save command-line input arguments. */
                for (String s : args) {
                    inputArgs.append(" " + s);
                }
                inputArgs.append(" je.version=" + JEVersion.CURRENT_VERSION);
                System.out.println("nCommand-line input arguments:n "
                        + inputArgs);
                System.out.println("Test configurations:nt" + this + "n");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }


        }
        
        @Override
        public String toString() {
            return "TestConfig={ " +
                   " dirtyRead=" + dirtyRead +
                   " preload=" + preload +
                   " deferredWrite=" + deferredWrite +
                   " syncCommit=" + syncCommit +
                   " useTxns=" + useTxns +
                   " numberThreads=" + numThreads +
                   " numOperations=" + numOperations +
                   " operationType=" + operationType +                   
                   " itemsPerTxn=" + itemsPerTxn +
                   " logFileSize=" + logFileSize +
                   " cacheSize=" + cacheSize +
                   " envHome=" + envHome + " }";
        }

		public static String getStoreName() {
			return storeName;
		}

		public static void setStoreName(String storeName) {
			TestConfig.storeName = storeName;
		}

        
    }
}
