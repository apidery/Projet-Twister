function Message(id, id_mes, auteur, login_auteur, texte, date, comments, nbComments, likes, nbLikes, likeByMe) {
    this.id_message = id_mes;
    this.id = id;
    this.auteur = auteur;
    this.login_auteur = login_auteur;
    this.text = texte;
    this.date = date;

    // Comments
    if(comments == undefined) {
        this.comments = new Set();
        this.nbComments = 0;
    }else{
    	this.comments = comments;
        this.nbComments = nbComments;
    }

    //Likes
    if(likes == undefined){
        this.likes = new Set();
        this.nbComments = 0;
    }else{
        this.likes = likes;
        this.nbLikes = nbLikes;
    }

    this.likeByMe = likeByMe;
}

 Message.prototype.getHtml = function (){
        msg = "<div class=\"msg\"> <div class=\"text_msg\"> <p>"+this.text+"</p></div>";
        
        if(this.auteur == env.id){
        	msg += "<div action=\"javascript:(function(){return;})()\" onClick=javascript:deleteMessage('"+this.id_message+"') class=\"trash\"> <img src=\"img/trash.png\"\></img> </div>";
        }

        msg += "<div class=\"auteur_msg\" action=\"javascript:(function(){return;})()\" onclick=\"javascript:profil_follower('"+this.login_auteur+"')\" > posté par : "+this.login_auteur+"</div>\
            <div class=\"date_msg\"> le "+this.date+" </div>";

        // Affichage d'un coeur rouge si le message n'est pas encore liké, noir si il est déjà liké.
        if(this.likeByMe == true){
            msg += "<div class=\"like\"> <span>"+this.nbLikes+"</span> <img onclick=\"javascript:like('"+this.id_message+"')\" src=\"img/dislike.png\"></img></div>";
        }else{
            msg += "<div class=\"like\"> <span>"+this.nbLikes+"</span> <img onclick=\"javascript:like('"+this.id_message+"')\" src=\"img/like.png\"></img></div>";
        }
            
        msg += "<div class=\"commentaire\"><div class=\"zone_commentaire\">\
                    <form id=\"new_comment"+this.id+"\" method=\"GET\" action=\"javascript:(function(){return;})()\" onSubmit=\"javascript:nouveauCommentaire(this,'"+this.id_message+"', '"+this.id+"')\">\
                        <textarea name=\"post_comment\" form=\"new_comment"+this.id+"\" placeholder=\"Réagir\"></textarea>\
                        <input class=\"poster\" type=\"submit\" value=\"Commenter\"/>\
                    </form>\
                </div>\
            <div class=\"list_commentaire\">";
 
        if(comment_d == this.id){
            for(let item of this.comments){
                msg+= item.getHtml();
            }   
              msg+= "<div class=\"voir_commentaires\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:hideComment()\" > <p> Cacher les commentaires </p></div>";     
        }
        else{
            if(this.nbComments!=0){
                msg+= "<div class=\"voir_commentaires\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:showComment('"+this.id+"')\" > <p> Voir les commentaires </p></div>";    
            }
        }
      
       msg += "</div>\
        </div>\
        </div>";

        return msg;
 }


function printMessage(iduser){
    s = "";

    if(iduser==undefined){
        for (let item of env.msgs){
        	s+= item.getHtml();
        }
    }else{
        for (let item of env.msgs){
            if(item.auteur==iduser){
                s+= item.getHtml();
            }
        }
    }

    if(env.msgs.size != env.nbMessagesTotal){
        s+= "<div id=\"more_message\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:more_messages()\"> <p> Voir plus </p> </div>";
    }else{
        s+= "<div id=\"more_message\" action=\"javascript:(function(){return;})()\" onClick=\"javascript:less_messages()\"> <p> Voir moins </p> </div>"
    }

    $("#messages").html(s);
}

function printMessageProfil(iduser){
     s = "";

    if(iduser==undefined){
        for (let item of env.msgsProfil){
            s+= item.getHtml();
        }
    }else{
        for (let item of env.msgsProfil){
            if(item.auteur==iduser){
                s+= item.getHtml();
            }
        }
    }

    $("#messages").html(s);
}


function revival(key, value){

    if(value.id != undefined){
        var c = new Message(value.id, value.id_message, value.user, value.user_login, value.content, value.date, value.comments, value.nb_comments, value.likes, value.nbLikes, value.like_by_me);
        return c;
    }else if(value.id_commentaire != undefined){
        var c = new Comment(value.id_commentaire, value.id_message, value.user_id, value.user_login, value.content, value.date);
        return c;
    }else{
        return value;
    }


}



function completeMessage(){
    if(connection){
        $.ajax({
            type : "GET",
            url : "message/GetMessages",
            data : "key="+env.key+"&query="+env.query+"&id_max="+env.maxId+"&id_min="+env.minId+"&nb="+env.nb,
            async : false,
            success : function (rep){
                completeMessagesReponse(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                func_erreur(rep.message);
            },
        });
    }
}


function completeMessagesReponse(rep) {

    if (rep.error == undefined) {
        var listeMessages = JSON.parse(rep,revival);

        env.nb_msg = rep.nbMessages;     

        env.msgs.clear();

        for (var i=0; i < listeMessages.messages.length; i++) {
            
            env.msgs.add(listeMessages.messages[i]);

            if(listeMessages.messages[i].id_message > env.maxId){
                env.maxId = listeMessages.messages[i].id_message;
            }
            if(env.minId < 0 || listeMessages.messages[i].id_message < env.minId){
                env.minId = listeMessages.messages[i].id_message;
            }
        } 

    } else {
        func_erreur(rep.message);
    }

    printMessage();
}   


function listMessageUser(){
    if(connection){
        $.ajax({
            type : "GET",
            url : "message/GetMessagesUser",
            data : "key="+env.key+"&id_user="+env.id,
            success : function (rep){
               reponseMessageUser(rep);
               completeMessage();
            },
            error : function (jqXHR, textStatus, errorT){
                func_erreur(rep.message);
            },
        });
    }
}

function onlyMessageUser(iduser){
    if(connection){
        $.ajax({
            type : "GET",
            url : "message/GetMessagesUser",
            data : "key="+env.key+"&id_user="+iduser,
            success : function (rep){
               reponseMessageUser(rep,iduser);
            },
            error : function (jqXHR, textStatus, errorT){
               func_erreur(rep.message);
            },
        });
    }
}

function reponseMessageUser(rep, iduser){
    if(connection){
         var listeMessages = JSON.parse(rep,revival);
        
        env.msgsProfil.clear();

        for (var i=0; i < listeMessages.messages.length; i++) {   
           env.msgsProfil.add(listeMessages.messages[i]); 
        }

        printMessageProfil(iduser);     
    }
}

function nouveauMessage(formulaire) {
    var texte = formulaire.new_message.value;

    if(connection){
         $.ajax({
            type:"GET",
            url: "message/AddMessage",
            data:"key=" + env.key + "&content=" + texte,
            dataType: "JSON",
            success: function(rep) {
               nouveauMessageReponse(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                func_erreur(rep.message);
            },
        });
    }
   
}


function nouveauMessageReponse(rep){
    if((rep != undefined) && (rep.error == undefined)){
        env.maxId = rep.id_message;
    	completeMessage();
    }else{
        func_erreur(rep.error);
    }
}


function deleteMessage(id){
	 if(connection){
         $.ajax({
            type:"GET",
            url: "message/DeleteMessage",
            data:"key=" + env.key + "&idmessage="+id,
            dataType: "JSON",
            success: function(rep) {
                reponseDeleteMessage(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                func_erreur(textStatus);
            },
        });
    }
}


function reponseDeleteMessage(rep){
    if(rep.error == undefined){
        completeMessage();
    }
	else{
        func_erreur(rep.error);
    }
}


function like(idmessage){
    if(connection){
         $.ajax({
            type:"GET",
            url: "message/AddLike",
            data:"key=" + env.key + "&idmessage="+idmessage,
            dataType: "JSON",
            success: function(rep) {
                reponseLike(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                func_erreur(textStatus);
            },
        });
    }
}

function reponseLike(rep){
    if(rep.error == undefined){
        completeMessage();
    }
}

function more_messages(){
    var old = env.nb;
    env.nb = old + 4;
    env.minId = -1;
    env.maxId = -1;

    completeMessage();
}

function less_messages(){
    env.nb = 4;
    env.minId = -1;
    env.maxId = -1;

    completeMessage();
}