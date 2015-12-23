# Dynamic Public Opinion
Progetto di tesi sperimentale per la Laurea Magistrale: <i>Dynamic public opinion: strumenti software di analisi e casi studio su reti sociali</i> <br>
Università degli Studi di Roma Tre - Dip. di Ingegneria Informatica <br>
(Anno Accademico 2014-2015)

a cura di: <i>Lorenzo Martucci</i>

Dynamic opinion legata ad eventi all'interno del social network Twitter

------------------------------------------------------------------------

# Procedimento e analisi del software

Le principali fasi del progetto sono le seguenti:
- <b>Reperimento delle news</b> <br>
Grazie ad un altro progetto, del quale è riportata solo una classe all'interno del package <i>googleNews</i>, si scaricano e si indicizzano le varie notizie, in seguito dai vari titoli vengono estrapolate le entità che rappresenteranno le keywords delle query da lanciare all'interno del social network;
- <b>Raccolta dei tweet e degli utenti</b> <br>
Tramite le API di Twitter si ottengono tweet che riguardano la notizia in questione e in seguito si raccolgono tutti gli utenti in modo da ricercare, nel passato, opinioni/tweet sempre sullo stesso argomento;
- <b>Analisi per la formazione del training</b> <br>
Fase di analisi e di studio di tutti i tweet raccolti per la formazione di un insieme di addestramento da poter passare al classificatore;
- <b>Machine Learning</b> <br>
Insiemi di training e di testing per il classificatore Naive-Bayes in modo da ottenere risultati e valutazioni finali.

<b><i>Package</i></b> <br>
Breve descrizione delle funzionalità delle varie classi java all'interno dei packge:
- <b>cleanForFile</b> <br>
<i>StopwordsPoints</i>: pulisce i tweet da alcuni segni di punteggiatura e rimuove le parole contenute nei file stopword (presenti in tre lingue differenti: italiana, inglese e spagnola);
- <b>database</b> <br>
<i>OperationMongoDB</i>: sono presenti diverse operazioni da poter effettuare su MongoDB; il resto delle classi riguardano le operazioni su Neo4J, in particolare: <i>TwitterNeo4j</i> permette di inserire gli oggetti "Status" di Twitter all'interno del graphDB, mentre <i>TwitterNeo4jFromMe</i> è una variante della classe precedente e prende tweet di tipo Stringa (esempio di formato in CasiStudio/DatiWindows10/datiInizialiNeo.txt) e li inserisce sempre all'interno del database;
- <b>experiment</b> <br>
le classi principali sono <i>Cont</i> e <i>ContSentiment</i>: la prima dato un file di testo conta retweet, hashtag, menzioni e utenti, la seconda conta invece i "sentiment";
- <b>googleNews</b> <br>
<i>GoogleNewsCorpusRetriever</i>: classe estrapolata dal progetto GoogleNews, contiene l'indicizzazione delle notizie, la stampa dei risultati e il salvataggio dei titoli in un file di testo;
- <b>naiveBayes</b> <br>
qui ci sono le classi java del classificatore Naive-Bayes: gli insiemi di training e di testing devono essere inseriti all'interno di <i>RunnableExample</i>; inoltre la classe <i>Parser</i> permette di preparare i file di testo del training e del testing nel formato adatto al classificatore Naive-Bayes;
- <b>otherProceedings</b> <br>
qui ci sono tutte classi di prova, per vari esperimenti iniziali, che permettono di comprendere i vantaggi e i limiti delle API di Twitter e le potenzialità e gli errori della sentiment analysis;
- <b>pos</b> <br>
sono contenute tutte le classi per il part of speech, tra cui:
<i>TweetTextMain</i> e <i>Tagger</i> dove sono presenti degli esempi per capire il loro funzionamento;
<i>Tagme</i> che recupera le entità dai titoli e scrive su un file di testo quelle più numerose;
- <b>query</b> <br>
classi per le analisi del sentiment all'interno dei tweet; classi per il recupero dei tweet tramite le Streaming API;
<i>MoreSearchQuery</i>: classe definitiva utilizzata per il recupero dei tweet;
<i>TweetPast</i>: data una lista di utenti ricerca i loro tweet precedenti ad una data indicata;
- <b>sentiment</b> <br>
classi dedicate alla sentiment analysis: SentiWordNet e SentiStrength (per quest'ultimo è previsto un dizionario inglese, italiano e spagnolo);
- <b>twitter4j</b> <br>
<i>MainTwitter</i>: contiene diversi metodi che utilizzano le API di Twitter.







