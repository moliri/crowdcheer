<?php

require('vendor/autoload.php');
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

$app = new Silex\Application();
$app['debug'] = true;

//database initial
$dbopts = parse_url(getenv('DATABASE_URL'));
// Register the Database service
$app->register(new Herrera\Pdo\PdoServiceProvider(),
  array(
    'pdo.dsn' => 'pgsql:dbname='.ltrim($dbopts["path"],'/').';host='.$dbopts["host"],
    'pdo.port' => $dbopts["port"],
    'pdo.username' => $dbopts["user"],
    'pdo.password' => $dbopts["pass"]
  )
);

// Register the monolog logging service
$app->register(new Silex\Provider\MonologServiceProvider(), array(
  'monolog.logfile' => 'php://stderr',
));

// Our web handlers
$app->get('/', function() use($app) {
  $app['monolog']->addDebug('logging output.');
  return 'Running';
});

$app->post('/send-message', function(Request $request) {
	$message = $request->get('message');
	$number = $request->get('number');
	require('../web/class.googlevoice.php');
	$gv = new GoogleVoice("crowdcheer@gmail.com", "crowdcheersccs");
	$gv->sms($number, $message);
	return new Response('Post',201);
});

$app->get('/send-message', function() use($app) {
  $app['monolog']->addDebug('logging output.');
  return 'get';
});

/*//Database initial
$app->get('/db/initial',funtion() use($app){
	$st = $app['pdo']->prepare('CREATE TABLE user(
			UID		INT PRIMARY KEY  NOT NULL,
			Phone				CHAR(20),
			Nickname			TEXT,
			VerifyCode			INT
		)');
	try {
		$st->execute(); 
	} catch (PDOException $e) {
		return $st->errorCode();
	}
	return 'done';
})
//Database create new user
$app->post('/db/new-user',function(Request $request) {
	$number = $request->get('number');
})*/

$app->run();

?>
