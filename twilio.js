
function motivate(){
// Twilio Credentials 
var accountSid = 'ACbbb8c6e3d5e297756ff33332f8e5d764'; 
var authToken = 'd99dc9a137db2f08404ad65c33c02027'; 
 
//require the Twilio module and create a REST client 
var client = require('twilio')(accountSid, authToken); 
 
client.messages.create({ 
	to: "5712941193", 
	from: "+12405605233", 
	body: "keep running",   
}, function(err, message) { 
	console.log(message.sid); 
});

}