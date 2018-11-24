function profil_user(){
	$("body").load("html/profil_user.html");
}

function profil_follower(login){
	if(login == env.login){
		profil_user();
	}else{
        $.ajax({
            type : "GET",
            url : "user/InfoUser",
            data : "login="+ login,
            dataType : "JSON",
            success: function (rep){
                reponseProfilFollower(rep);
            },
            error: function (jqXHR, textStatus, errorT){
                alert("error de les profils");
            }
        });
	}
}

function reponseProfilFollower(rep){
		visit = new Object;	

		visit.id  = rep.id;
		visit.login = rep.login;
		visit.nom = rep.nom;
		visit.prenom = rep.prenom;
		visit.date_naiss = rep.date_naiss;
		visit.follows = new Set();
		visit.nb_abonne = rep.nbAbo;
		visit.msgs = new Set();

		print_profil_follower();
}

function print_profil_follower(){
	$("body").load("html/profil_follower.html");
}

// Exercice 

function changeColor(color){
	if(connection){
        $.ajax({
            type : "GET",
            url : "user/ChangeColor",
            data : "key="+ env.key+"&color="+color,
            dataType : "JSON",
            success: function (rep){
                reponseChangeColor(rep);
            },
            error: function (jqXHR, textStatus, errorT){
                alert("error dans le profil");
            }
        });
	}
}

function reponseChangeColor(rep){
	if(rep.error == undefined){
		env.colorProfil = rep.color;   
	}
}