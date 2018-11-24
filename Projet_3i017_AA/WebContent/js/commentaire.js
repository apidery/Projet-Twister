function Comment(id, idmessage, auteur, login_auteur, texte, date) {
    this.idcomment = id;
    this.idmessage = idmessage;
    this.auteur = auteur;
    this.login_auteur = login_auteur;
    this.text = texte;
    this.date = date;
}

Comment.prototype.getHtml = function (){
        cmt = "<div class=\"cmt\">\
        		 <div class=\"text_cmt\">";

        if(this.auteur == env.id){
        	cmt += "<p class=\"link_delet_comment\" action=\"javascript:(function(){return;})()\" onClick=javascript:deleteComment('"+this.idcomment+"','"+this.idmessage+"')> Supprimer </p>";
        }
        
        cmt += "<span onclick=\"javascript:profil_follower('"+this.login_auteur+"')\">"+this.login_auteur+"</span><p>"+this.text+"</p></div>\
        	<div class=\"date_cmt\"> le "+this.date+" </div>\
           </div>";

        return cmt;
 }


function showComment(idmessage){
	comment_d = idmessage;
	nb_comment_show = 3;
	listMessageUser();
}

function hideComment(){
	comment_d = -1;
	nb_comment_show = 0;
	listMessageUser();
}

function nouveauCommentaire(formulaire, idmessage){

    var texte = formulaire.post_comment.value;

    if(connection){
         $.ajax({
            type:"GET",
            url: "comments/AddComment",
            data:"key=" + env.key + "&content=" + texte + "&idmessage=" + idmessage,
            dataType: "JSON",
            success: function(rep) {
                nouveauCommentaireReponse(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
    }
}

function nouveauCommentaireReponse(rep){
    completeMessage();
}

function deleteComment(idcomment, idmessage){
	if(connection){
         $.ajax({
            type:"GET",
            url: "comments/DeleteComment",
            data:"key=" + env.key + "&idcomment="+idcomment+"&idmessage="+idmessage,
            dataType: "JSON",
            success: function(rep) {
                reponseDeleteComment(rep);
            },
            error : function (jqXHR, textStatus, errorT){
                alert(textStatus);
            },
        });
    }
}

function reponseDeleteComment(rep){
   completeMessage();
}