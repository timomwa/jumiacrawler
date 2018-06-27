package ke.co.technovation.jumiacrawler;


import com.google.common.io.Files;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.flywaydb.core.Flyway;


public class SampleLauncher {
	
	public static void main(String[] args) throws Exception {

        String crawlStorageFolder = Files.createTempDir().getAbsolutePath();
        int numberOfCrawlers = Integer.valueOf(1);

        CrawlConfig config = new CrawlConfig();

        config.setPolitenessDelay(10);

        config.setCrawlStorageFolder(crawlStorageFolder);

        //config.setMaxPagesToFetch(3);
        config.setIncludeHttpsPages(true);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("https://www.jumia.co.ke");
        //controller.addSeed("https://de.wikipedia.org/wiki/Relationale_Datenbank");
        //controller.addSeed("https://pt.wikipedia.org/wiki/JDBC");
        //controller.addSeed("https://pt.wikipedia.org/wiki/Protocolo");
        //controller.addSeed("https://de.wikipedia.org/wiki/Datenbank");


        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost/crawler", "crawler-admin", "crawler-admin");
        flyway.migrate();


        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass("org.postgresql.Driver");
        pool.setJdbcUrl("jdbc:postgresql://localhost/crawler");//args[1]);
        pool.setUser("crawler-admin");
        pool.setPassword("crawler-admin");
        pool.setMaxPoolSize(numberOfCrawlers);
        pool.setMinPoolSize(numberOfCrawlers);
        pool.setInitialPoolSize(numberOfCrawlers);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(new PostgresCrawlerFactory(pool), numberOfCrawlers);

        pool.close();
    }

}
