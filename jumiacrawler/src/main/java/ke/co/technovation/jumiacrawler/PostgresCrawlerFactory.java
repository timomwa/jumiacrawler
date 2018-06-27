package ke.co.technovation.jumiacrawler;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import ke.co.technovation.jumiacrawler.db.PostgresDBServiceImpl;

public class PostgresCrawlerFactory implements CrawlController.WebCrawlerFactory<PostgresWebCrawler> {
	
	private ComboPooledDataSource comboPooledDataSource;

    public PostgresCrawlerFactory(ComboPooledDataSource comboPooledDataSource) {
        this.comboPooledDataSource = comboPooledDataSource;
    }

    public PostgresWebCrawler newInstance() throws Exception {
        return new PostgresWebCrawler(new PostgresDBServiceImpl(comboPooledDataSource));
    }

}
