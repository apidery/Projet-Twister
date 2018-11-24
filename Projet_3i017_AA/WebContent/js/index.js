function init(){
	env = new Object();
	connection = false;
	comment_d = -1;
	nb_comments_show = 0;
}

function makeMainPanel(){

	if(connection){	
		env.fromId = -1;
	    env.query = undefined;
	    env.minId = -1;
		env.maxId = -1;
		env.nb = 4;
	}
	
	$("body").load("html/page_principale.html");
}
