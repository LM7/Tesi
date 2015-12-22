package googleNews;
/*


import gate.creole.annic.apache.lucene.search.TopDocs;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.tabularium.net.downloader.DefaultResourceFactory;
import org.tabularium.net.downloader.Downloader;
import org.tabularium.net.downloader.InMemoryQueue;
import org.tabularium.net.downloader.Resource;
import org.tabularium.text.html.BoilerPipeLargestContentExtractor;
import org.tabularium.text.html.BoilerPipeNewsContentExtractor;
import org.tabularium.text.se.FieldLuceneSearchEngine;
import org.tabularium.text.se.LuceneSearchEngine;
import org.tabularium.text.se.SqlRawStorer;
import org.tabularium.text.se.UrlFieldAnalyzer;
import org.tabularium.um.common.UrlTimestamp;
import org.tabularium.um.html.eval.EvalDownloaderListener;
//import org.tabularium.um.common.Storers;
//import org.tabularium.um.common.UrlTimestamp;
//import org.tabularium.um.html.extractor.BrowsingHistory;
//import org.tabularium.um.html.news.DataRecordGoogleNewsRetriever;
//import org.tabularium.um.html.news.News;
import org.tabularium.um.html.news.DataRecordGoogleNewsRetriever;
import org.tabularium.um.html.news.News;

public class GoogleNewsCorpusRetriever {
	protected BoilerPipeNewsContentExtractor newsContentExtractor;
	protected BoilerPipeLargestContentExtractor wholePageContentExtractor;

	protected Set<String> alreadyProcessedNcls = new HashSet<String>();
	protected Set<String> storedNewsUrls = new HashSet<String>();;		
	protected List<String> evalGoogleNewsTopics;
	
	
	protected HtmlCleaner htmlCleaner;

	protected int maxNcls;

	protected int minNewsPerNcl;

	protected int waitTimeBetweenMainLoop;

	public static final int DEFAULT_MAX_NCLS = 30; // sarebbe 30

	public static final int DEFAULT_MIN_NEWS_PER_NCL = 1000;

	// see https://developers.google.com/news-search/v1/jsondevguide
	public static final String DEFAULT_GOOGLENEWSTOPICS = "w b n t el p e s m";

	public static final String DEFAULT_PROXYHOSTNAME = "";

	public static final int DEFAULT_PROXYPORT = 4001;

	// secs to wait between the main loop on the topics
	// so that multiple clusters of similar news have less chance to be
	// retrieved
	public static final int DEFAULT_WAITTIME_BETWEEN_MAIN_LOOP = 86400; // 1 day

//	protected GoogleNewsSessionBuilder sessionBuilder;

	protected Preferences prefs;

	protected InMemoryQueue queue;

	protected Downloader googleDownloader;

	protected Downloader newsDownloader;

	protected EvalDownloaderListener downloaderListener;

	private static GoogleNewsCorpusRetriever instance = null;

	private static Logger logger = Logger
			.getLogger(GoogleNewsCorpusRetriever.class.getSimpleName());

	private GoogleNewsCorpusRetriever() {
		// try {
		// init();
		// } catch (Exception e) {
		// logger.severe(e.toString());
		// System.exit(1);
		// }
	}

	public static GoogleNewsCorpusRetriever getInstance() {
		if (instance == null)
			instance = new GoogleNewsCorpusRetriever();
		return instance;
	}

	public void init() throws Exception {
		System.out.println("DENTRO INIT");
		prefs = Preferences.userRoot().node("/org/tabularium/um/cues");

		maxNcls = prefs.getInt("evalMaxNumberOfNcls",
				GoogleNewsCorpusRetriever.DEFAULT_MAX_NCLS);

		minNewsPerNcl = prefs.getInt("evalMinNewsPerNcl",
				GoogleNewsCorpusRetriever.DEFAULT_MIN_NEWS_PER_NCL);

		String s = prefs.get("evalGoogleNewsTopics",
				GoogleNewsCorpusRetriever.DEFAULT_GOOGLENEWSTOPICS);
		evalGoogleNewsTopics = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(s);
		while (tokenizer.hasMoreTokens()) {
			evalGoogleNewsTopics.add(tokenizer.nextToken());
			System.out.println("DENTRO IL PRIMO WHILE DI INIT");
		}
		waitTimeBetweenMainLoop = prefs.getInt("evalWaitTimeBetweenMainLoop",
				GoogleNewsCorpusRetriever.DEFAULT_WAITTIME_BETWEEN_MAIN_LOOP);

		boolean reset = prefs.getBoolean("reset", false);

		String proxyHostname;
		int proxyPort;

		// downloader for google requests
		queue = new InMemoryQueue();
		googleDownloader = new Downloader(queue);
		googleDownloader.setMaxThreads(1);
		googleDownloader.setFollowRedirect(true);
		googleDownloader.setMaxRedirectAttempts(10);
		googleDownloader.setTimeout(120000);
		googleDownloader.setMaxSize(320 * 1024);
		s = prefs
				.get("downloaderUserAgent",
						"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0");
		googleDownloader.setUserAgent(s);

		proxyHostname = prefs.get("googleRequestsProxyHostname",
				DEFAULT_PROXYHOSTNAME);
		proxyPort = prefs.getInt("googleRequestsProxyPort", DEFAULT_PROXYPORT);
		if ((proxyHostname != null) && (proxyHostname.length() > 0)) {
			logger.info("using proxy setting for google requests: "
					+ proxyHostname + ":" + proxyPort);
			googleDownloader.setProxy(proxyHostname, proxyPort);
		}
		downloaderListener = new EvalDownloaderListener();
		googleDownloader.addListener(downloaderListener);

		// downloader for news pages, usually less polite
		queue = new InMemoryQueue();
		newsDownloader = new Downloader(queue);
		newsDownloader.setMaxThreads(10);
		newsDownloader.setFollowRedirect(true);
		newsDownloader.setMaxRedirectAttempts(1);
		newsDownloader.setTimeout(120000);
		newsDownloader.setMaxSize(320 * 1024);
		s = prefs
				.get("downloaderUserAgent",
						"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0");
		newsDownloader.setUserAgent(s);

		proxyHostname = prefs.get("proxyHostname", DEFAULT_PROXYHOSTNAME);
		proxyPort = prefs.getInt("proxyPort", DEFAULT_PROXYPORT);
		if ((proxyHostname != null) && (proxyHostname.length() > 0)) {
			logger.info("using proxy setting: " + proxyHostname + ":"
					+ proxyPort);
			newsDownloader.setProxy(proxyHostname, proxyPort);
		}

		newsContentExtractor = new BoilerPipeNewsContentExtractor();
		wholePageContentExtractor = new BoilerPipeLargestContentExtractor();
		htmlCleaner = new HtmlCleaner();
		System.out.println("FINE INIT");

//		if (reset) {
//			logger.info("resetting the whole storer (current number of stored clusters "
//					+ Storers.getInstance().numberOfClusters()
//					+ " news "
//					+ Storers.getInstance().numberOfNews() + ")");
//			Storers.getInstance().clearAll();
//			logger.info("resetting done (current number of stored clusters "
//					+ Storers.getInstance().numberOfClusters() + " news "
//					+ Storers.getInstance().numberOfNews() + ")");
//		}
//
//		sessionBuilder = new GoogleNewsSessionBuilder();
//		sessionBuilder.init();
	}

	/**
	 * By calling this method the preferences for the other singleton classes
	 * are automatically loaded.
	 * 
	 * @param is
	 * @throws Exception
	 */

/*
	public void init(InputStream is) throws Exception {
		Preferences.importPreferences(is);
		init();
	}
	
	public boolean isIndexed(String url) {
		return storedNewsUrls.contains(url);
	}

	public void retrieve() throws Exception {
		int newscont = 0;
		System.out.println("DENTRO RETRIEVE");
		// htmlcleaner
		CleanerProperties htmlCleanerProps = new CleanerProperties();
		SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(
				htmlCleanerProps);

		htmlCleanerProps.setOmitUnknownTags(true);
		htmlCleanerProps.setOmitComments(true);
		htmlCleanerProps.setUseEmptyElementTags(false);

		DataRecordGoogleNewsRetriever gns = DataRecordGoogleNewsRetriever
				.getInstance();
		gns.init(googleDownloader, newsDownloader);

		int nclStored = 0;
		//Set<String> alreadyStoredNcls = Storers.getInstance().retrieveOrderedNcls();
		nclStored = alreadyProcessedNcls.size();
		if (nclStored > 0) {
			logger.info(nclStored
					+ " ncls (clusters of news) already stored in the db");
			//sessionBuilder.setNclsToAvoid(alreadyStoredNcls);
		}

		long lastGoogleTopicRequests = 0;
		
		int prova = 0;
		System.out.println("MAXNCLS "+ maxNcls);
		maxNcls = 1;
		System.out.println("MAXNCLS "+ maxNcls);
		while (nclStored < maxNcls) {
			System.out.println("NEL WHILE nclStored "+ nclStored);
			System.out.println("NEL WHILE MAXNCLS "+ maxNcls);
			System.out.println("DENTRO IL PRIMO WHILE DI RETRIEVE");
			Iterator<String> googleTopicIter = evalGoogleNewsTopics.iterator();
			for (int i = 0; googleTopicIter.hasNext() && nclStored < maxNcls; i++) { //modifica for
				System.out.println("DENTRO IL FOR DI RETRIEVE");
				String topic = (String) googleTopicIter.next();
				if ((System.currentTimeMillis() - lastGoogleTopicRequests) < 60000) {
					try {
						logger.info("falling asleep between google requests "
								+ 60000 + "msecs");
						Thread.sleep(60000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}

				lastGoogleTopicRequests = System.currentTimeMillis();
				String googleTopicUrl = gns.buildTopicUrl(topic);
				logger.info("topic category: " + topic);
				Set<String> ncls = gns.retrieveClusters(topic);
				logger.info("found " + ncls.size()
						+ " ncls (clusters of similar news)");
				// logger.info("retrieving google topic page: " +
				// googleTopicUrl);
				// List<String> urls = new ArrayList();
				// urls.add(googleTopicUrl);
				// googleDownloader.addURLs(urls);
				// googleDownloader.start();
				// googleDownloader.waitDone();
				// String googleTopicPage =
				// downloaderListener.getPage(googleTopicUrl);
				// String listenerError =
				// downloaderListener.errors.get(googleTopicUrl);
				// downloaderListener.reset();
				// if (googleTopicPage == null) {
				// logger.severe("unable to downlaod "+googleTopicUrl+" "+listenerError);
				// continue;
				// }
				//
				// // PrintWriter out = new
				// //
				// PrintWriter("GoogleNewsCluster"+DEFAULT_GOOGLE_TOPICS[i]+".html");
				// // out.print(new String(res.getObject()));
				// Set<String> ncls = gns.retrieveClusters(googleTopicPage);
				//
				//

				Iterator<String> it = ncls.iterator();
				while (it.hasNext() && (nclStored < maxNcls)) {
					System.out.println("DENTRO IL SECONDO WHILE DI RETRIEVE");
					String ncl = it.next();
					// if previous run has been stopped on a specific ncl, skip
					// it to avoid duplicates
					// if (prevUrls.size() > 0) {
					// logger.info("ncl "+ncl+" already analyzed in a previous run so i skip it ");
					// continue;
					// }
					logger.info("retrieving headers of ncl (cluster): " + ncl);
					// retrieve headers from a ncl
					Set headers = gns
							.retrieveHeaders(
									ncl,
									0,
									DataRecordGoogleNewsRetriever.DEFAULT_GOOGLE_TOPICS[i]);
					logger.info("found headers #" + headers.size());
					if (headers.size() < minNewsPerNcl) {
						logger.info("skipping cluster of news " + ncl
								+ " because there are too few ("
								+ headers.size() + " < " + minNewsPerNcl + ")");
						continue;
					}
					logger.info("start downloading headers");
					// retrieve news from a list of headers
					System.out.println("PRIMA DI GNS");
					Set<org.tabularium.um.html.news.News> hds = gns.retrieveNews(headers); // QUI!
					System.out.println("DOPO DI GNS");
					logger.info("successfully retrieved news #" + hds.size());
					System.out.println("DOPO IL LOGGER");
					News news;
					HashSet alreadyProcessedUrls = new HashSet<String>();
					Iterator<News> iterNews = hds.iterator();
					while (iterNews.hasNext() && prova < 10) { //modifica while
						System.out.println("DENTRO IL TERZO WHILE DI RETRIEVE");
						System.out.println("PROVA "+prova);
						news = iterNews.next();
						prova = prova+1;
						if (isIndexed(news.getUrl())) {
							alreadyProcessedUrls.add(news.getUrl());
							logger.info("skipping already indexed url (ncl"
									+ ncl + "): " + news.getUrl());
							continue;
						}
						long timestamp = System.currentTimeMillis();
						UrlTimestamp urlts = new UrlTimestamp(news.getUrl(),
								timestamp);

						String htmlContent, newsContent, textContent;
						// make it tidy
						try {
							TagNode tagNode = htmlCleaner.clean(news.getDoc());
							htmlContent = htmlSerializer.getAsString(tagNode);
						} catch (Exception e) {
							logger.info("skipping page unable to tidy url (ncl"
									+ ncl + "): " + news.getUrl() + " ex:"
									+ e.toString());
							continue;
						}

						// boilerpipe text extraction
						try {
							newsContent = newsContentExtractor.extractText(
									news.getUrl(), htmlContent);
							textContent = wholePageContentExtractor
									.extractText(news.getUrl(), htmlContent);
						} catch (Exception e) { // BoilerpipeProcessingException
							logger.info("skipping page boilerpipe extraction failed (ncl"
									+ ncl
									+ "): "
									+ news.getUrl()
									+ " ex:"
									+ e.toString());
							continue;
						}
						if (newsContent == null || textContent == null
								|| newsContent.trim().length() == 0
								|| textContent.trim().length() == 0) {
							logger.info("skipping page because news extraction failed (ncl"
									+ ncl + "): " + news.getUrl());
							continue;
						}

						// es. Field(name, value, stored, indexed, tokenized)
						Set<Field> fields = new HashSet();
						// i won't tokenize text that will not be queried for
						// precision/recall evaluation
						// i won't index metadata that will not be
						// searched/queried
						// see Storers.java for per-field Lucene Analyzers
						fields.add(new Field("ncl", LuceneSearchEngine
								.escape(ncl), true, true, false));
						fields.add(new Field("url", LuceneSearchEngine
								.escape(news.getUrl()), true, true, false));
						fields.add(new Field("timestamp", Long
								.toString(timestamp), true, true, false));
						fields.add(new Field("snippet-raw", news.getSnippet(),
								true, false, false));
						fields.add(new Field("snippet-indexed", news
								.getSnippet(), true, true, true));
						// untidy html source
						fields.add(new Field("raw-source", news.getDoc(), true,
								false, false));
						fields.add(new Field("tidy-source", htmlContent, true,
								false, false));
						fields.add(new Field("newspaper", LuceneSearchEngine
								.escape(news.getSource()), true, true, false));
						fields.add(new Field("title-indexed", news.getTitle(),
								true, true, true));
						fields.add(new Field("news-content", newsContent, true,
								true, true));
						fields.add(new Field("title-raw", news.getTitle(),
								true, false, false));
						fields.add(new Field("topic", news.getTopic(), true,
								true, false));
						fields.add(new Field("pos", Integer.toString(news
								.getPos()), true, false, false));

						URL u = null;
						try {
							u = new URL(news.getUrl());
						} catch (Exception ex) {
							logger.info("url malformed, skipping url (ncl"
									+ ncl + "): " + news.getUrl());
							continue;
						}
						String hostname = u.getHost().toLowerCase();
						fields.add(new Field("hostname", hostname, true, true,
								false));
						// hostname table will be altered during Session
						// building
						// Storers.getInstance().addHostname(hostname);

						//Storers.getInstance().addHostnameNews(hostname, urlts);
						// Storers.getInstance().addHostnameUrl(hostname,
						// urlTimestamp);

						// index the news with the new url+timestamp identifier
						newscont = newscont+1;
						index(LuceneSearchEngine.escape(news.getUrl()), textContent, fields);
						System.out.println("NEWS NUMERO "+newscont);
						System.out.println("PROVA PER VEDERE LE CARATTERISTICHE DI NEWS");*/
						/*System.out.println("DOC: "+news.getDoc());
						System.out.println("POS: "+news.getPos());
						System.out.println("SNIPPET: "+news.getSnippet());
						System.out.println("SOURCE: "+news.getSource());
						System.out.println("TITLE: "+news.getTitle());
						System.out.println("TOPIC: "+news.getTopic());
						System.out.println("URL: "+news.getUrl());*/
						//System.out.println("FINE CARATTERISTICHE NEWS");
						// stores a raw copy with the original url
						// (wo/timestamp)
//						Storers.getInstance().addRawPage(news.getUrl(),
//								news.getDoc(), timestamp);
//						Storers.getInstance().addNews(news.getUrl(), ncl,
//								timestamp);
				/*	}
					
					nclStored++;
					
					alreadyProcessedNcls.add(ncl);
//					Storers.getInstance().addOrderedNcl(ncl);
//					Storers.getInstance().addNcl(ncl);

					logger.info("stored ncls #" + nclStored + " of " + maxNcls);
					// sleep 10sec
					// Thread.sleep(10000);
				} // while (it.hasNext() && (nclStored < maxNcls))
					// sleep 1min
					// Thread.sleep(60000);
			} // for (int i = 0; googleTopicIter.hasNext(); i++)

			long t = this.waitTimeBetweenMainLoop * 1000l
					- (System.currentTimeMillis() - lastGoogleTopicRequests);     
			/*if (t > 0l) {
				try {
					logger.info("falling asleep in the main loop for "
							+ (t / 3600000l) + "hours");
					while (t > 0l) {
						Thread.sleep(300000l); // 5 mins
						t -= 300000l;
					}
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}*/ //COMMENTATO il tempo....
			// clear some memory
			//downloaderListener.reset();

		 /*} // while (nclStored < maxNcls)
	}
	
	void index(String url, String textContent, Set<Field> fields) throws IOException {
		//System.out.println("retrieved news (textContent="+textContent.length()+" chs): "+url);
		System.out.println("DENTRO L'INDEX");
		//indexwriter spostati da synchronized! creava sempre un nuovo indice sovrascriveva...
		IndexWriter writer;
		writer = new IndexWriter("./index/", new SimpleAnalyzer(), false); // se metto false non dovrebbe creare l'indice
		Document lucenedoc = new Document();
		// not tokenized but indexed
		if (url != null) {
			System.out.println("URL + "+url);
			lucenedoc.add(new org.apache.lucene.document.Field("url",
					url, true, true, false));
		}
		/*
		 * Constructs a String-valued Field that is: stored, indexed, tokenized
		 * and termvector will be stored as well.
		 */
		/*lucenedoc.add(new org.apache.lucene.document.Field("textContent", textContent, true,
				true, true, true));

		Iterator<Field> iter = fields.iterator();
		while (iter.hasNext()) {
			Field f = iter.next();
			lucenedoc.add(f);
		}

		// no one must delete while new docs are inserted
		synchronized (this) {
			System.out.println("PRIMA DELLA CARTELLA");
			writer.addDocument(lucenedoc);
			writer.close();
			System.out.println("OCCHIO ALLA STAMPA");
			IndexSearcher searcher = new IndexSearcher("./index/");
			Term term = new Term("url", url);
			Query query = new TermQuery(term);
			Hits hits = searcher.search(query);
			System.out.println("Number of hits: " + hits.length());
			if (hits.length() == 1) {
				System.out.println("UUUUUUUUUUUUUURL");
				System.out.println(url);
			}

		}
	}

	/*public static void main(String[] args) throws Exception {
//		LogManager.getLogManager().reset(); 
		
//		Logger globalLogger =
//		Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
//		globalLogger.setLevel(java.util.logging.Level.INFO);
		
		
		FileWriter file = new FileWriter("topic/ListaTitoli.txt");
		PrintWriter outFile = new PrintWriter(file);
		IndexSearcher searcher = new IndexSearcher("./index/");
		//String termineQuery = "http\\:\\/\\/www.businesstimes.com.sg\\/stocks\\/china\\-stocks\\-close\\-088\\-higher";
		//System.out.println(termineQuery);
		Term term = new Term("url", "http");
		PrefixQuery query = new PrefixQuery(term);
		Hits hits = searcher.search(query);
		System.out.println("Number of hits: " + hits.length());
		for (int i = 0; i < hits.length(); i++) {
			System.out.println(i);
			Document d = searcher.doc(i);
			System.out.println("||||||||||||INIZIO DOC||||||||||||");
			System.out.println("URL: "+d.get("url"));
			//System.out.println("TextContent: "+d.get("textContent")); a volte lungo
			System.out.println("FIELD:");
			System.out.println("NCL: "+d.get("ncl"));
			//System.out.println("url: "+d.get("url")); //inutile...
			System.out.println("TIMESTAMP: "+d.get("timestamp"));
			System.out.println("SNIPPET-RAW: "+d.get("snippet-raw"));
			System.out.println("SNIPPET-INDEXED: "+d.get("snippet-indexed"));
			//System.out.println("RAW-SOURCE: "+d.get("raw-source")); lungo...
			//System.out.println("TIDY-SOURCE: "+d.get("tidy-source")); lungo...
			System.out.println("NEWSPAPER: "+d.get("newspaper"));
			System.out.println("TITLE-INDEXED: "+d.get("title-indexed"));
			String titleIndexed = d.get("title-indexed");
			outFile.println(titleIndexed);
			//System.out.println("NEWS-CONTENT: "+d.get("news-content")); a volte lungo...
			System.out.println("TITLE-RAW: "+d.get("title-raw"));
			System.out.println("TOPIC: "+d.get("topic"));
			System.out.println("POS: "+d.get("pos"));
			System.out.println("------------FINE DOC-----------");
		}
		outFile.close();
		
		
		/*FileInputStream config = new FileInputStream("logging.properties");
		LogManager.getLogManager().readConfiguration(config);
		config.close();
		
		GoogleNewsCorpusRetriever r = new GoogleNewsCorpusRetriever();
		
		FieldLuceneSearchEngine se;
		while (true){ // to avoid exception that blocks the retrieval
		try {
			FileInputStream is = new FileInputStream("initialConfig.xml");
			r.init(is);
			r.retrieve();
			System.out.println("DONE");
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.toString());
		}
		}*/
		

	/*}
}*/



