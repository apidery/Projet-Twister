
/**
*Renvoi le liste des followers d'un utilisateur.
* @param login : login de l'utilisateur
*/
function listFollower(login){
	if(connection){
		$.ajax({
            type : "GET",
            url : "friend/ListFriend",
            data : "cle="+env.key+"&login="+login,
            dataType : "JSON",
            success : function (rep){
            	listFollowerReponse(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
	}
}

function listFollowerReponse(rep){

	var h = "";

	if(rep.error == undefined){
		if(connection){

            env.follows.clear();

			for(var i=0; i< rep.amis.length;i++){
				env.follows.add(rep.amis[i].idfriend);
			}
		}

		h+= "<h2 class=\"titre_info_profil\"> Abonnement </h2>\
            <span id=\"val_abonnement\">"+rep.amis.length+"</span>";

		$("#nb_abonnement").html(h);
	}	
}

function recommandation(){
	if(connection){
		$.ajax({
            type : "GET",
            url : "friend/Recommandation",
            data : "key="+env.key,
            dataType : "JSON",
            success : function (rep){
            	recommandationReponse(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
	}

}

function recommandationReponse(rep){

	var s = "";

	if(rep.error == undefined){
		for(var i=0; i<rep.membres.length; i++){
			s+="<div class=\"recommand\">\
                        <div class=\"login_member\">\
                            <span action=\"javascript:(function(){return;})()\" onclick=\"javascript:profil_follower('"+rep.membres[i].loginMembre+"')\" >"+rep.membres[i].loginMembre+"</span>\
                        </div>";

            if(rep.membres[i].ami == true){
            	s+= "<div class=\"icon_following\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:unfollow('"+rep.membres[i].loginMembre+ "', '"+rep.membres[i].idMembre+"')\">\
            			<img src=\"img/unfollow.png\"></img>";
            }else{
            	s+= "<div class=\"icon_following\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:follow('"+rep.membres[i].loginMembre+"' , '"+rep.membres[i].idMembre+"')\">\
            			<img src=\"img/follow.png\"></img>";	
            }
                          
            s+="</div></div>";
		}

		$("#membres").html(s);

        completeMessage();
	}else{
        makeConnexionPanel();
    }
}


function follow(login,id){
	if(connection){
		$.ajax({
            type : "GET",
            url : "friend/AddFriend",
            data : "key="+env.key+"&login="+login,
            dataType : "JSON",
            success : function (rep){
                listFollower(env.login);
                reponseFollow(rep);    
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
	}
}

function unfollow(login,id){
	if(connection){
		$.ajax({
            type : "GET",
            url : "friend/DeleteFriend",
            data : "cle="+env.key+"&login="+login,
            dataType : "JSON",
            success : function (rep){
                listFollower(env.login);
                reponseUnfollow(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
	}
}

function reponseFollow(rep){

	if(rep.error == undefined){
		recommandation();
	}
	else{
		alert("erreur");
	}
}

function reponseUnfollow(rep){

	if(rep.error == undefined){
		recommandation();
	}
	else{
		alert("erreur");
	}
}

