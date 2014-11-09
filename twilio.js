
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


function checkSpeed(goal, motivation){

var Speed = Parse.Object.extend("Speed");
var query = new Parse.Query(Speed);

query.descending("createdAt");
query.first({
  success: function(object) {
    // Successfully retrieved the object.
    if(object.get('speed') < goal){
        alert(object.get('speed') + " is less than the goal.")
        //twilio call
        var accountSid = 'ACbbb8c6e3d5e297756ff33332f8e5d764'; 
        var authToken = 'd99dc9a137db2f08404ad65c33c02027'; 
         
        //require the Twilio module and create a REST client 
        var client = require('twilio')(accountSid, authToken); 
         
        client.messages.create({ 
          to: "5712941193", 
          from: "+12405605233", 
          body: motivations,   
        }, function(err, message) { 
          console.log(message.sid); 
        });

      }
      else{
        alert(object.get('speed') + " is above the goal.")

      }

  },
  error: function(error) {
    alert("Error: No speed data yet");
  }
});
}