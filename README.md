# MoodTacker App

## I. Contexte

MoodTracker est une application mobile qui permet à son utilisateur noter son humeur du jour. Si l’utilisateur le souhaite, il peut ajouter un commentaire permettant d’expliciter la raison de son humeur du jour. L’historique des humeurs est également conservé, sur une plage de sept jours, leur permettant d’un coup d’œil de voir leur humeur générale. Chaque commentaire laissé peut également être consulté.
Avec MoodTracker, l’utilisateur peut mémoriser son humeur à tout moment au cours de la journée. A partir de minuit, l’humeur est mémorisée définitivement, et l’humeur du jour suivant est disponible.


## II.	Fonctionnement

### 2.1.	Premier lancement de l’application

Au premier lancement de l’application MoodTracker, l’écran d’accueil affiche par défaut une humeur joyeuse en plein écran symbolisée par le smiley suivant :

![2020-03-07_15-27-11](2020-03-07_15-27-11.png)
Figure 1 splash screen de MoodTracker


### 2.2.	Les différentes humeurs possibles


L’utilisateur a le choix entre cinq humeurs possibles :

![2020-03-07_15-33-13](2020-03-07_15-33-13.png)

En faisant glisser le doigt sur l’écran de bas en haut l’écran affichera l’humeur suivante, qui est plus joyeuse. En faisant glisser vers le bas, l’écran affichera l’humeur précédente, qui est moins joyeuse. Chaque humeur possède une couleur de fond différente (voir le tableau ci-dessus).

 ![2020-03-07_15-34-47](2020-03-07_15-34-47.png)
Figure 2: un bref aperçu de l'affichage des humeurs


### 2.3.	Mode d’utilisation de MoodTracker


Pour mémoriser une humeur, il faut procéder de manière suivante après le lancement de l’application :
•	En bas à gauche, un bouton permet d’ajouter un commentaire. Lorsque vous appuyez dessus, un popup s’affiche, avec une zone de texte et un clavier vous permettant de saisir du texte.
•	En bas à droite, un bouton permet d’accéder à l’historique de vos humeurs.

![2020-03-07_15-38-48](2020-03-07_15-38-48.png)
Figure 3: Ecran par défaut


Après avoir cliquer sur le bouton qui est en bas à droite, on obtient le popup suivant :

 ![2020-03-07_15-40-13](2020-03-07_15-40-13.png)
Figure 4: Écran pour ajouter un commentaire.

On saisit le commentaire et ensuite on peut cliquer sur le bouton « Validate » pour mémoriser le commentaire

![2020-03-07_15-41-50](2020-03-07_15-41-50.png)
Figure 5: Écran d'ajout d'un commentaire


En appuyant sur « Validate », message apparait sur l’écran pour nous indiquer si le commentaire a été bien enregistré

![2020-03-07_15-42-40](2020-03-07_15-42-40.png)
Figure 6: message toast si le commentaire est sauvegardé

### 2.4.	 Consulter l’historique

Pour consulter l’historique, il faut appuyer sur le bouton en bas à droite de l’écran. 
Si un commentaire n’était encore enregistré auparavant, on obtient l’affichage suivant :

![2020-03-07_15-44-35](2020-03-07_15-44-35.png)
Figure 7: pas d'humeur sauvegardée dans la base de données

Dans le cas contraire, sur l’écran d’historique, les sept dernières humeurs enregistrées sont affichées verticalement, de la plus ancienne à la plus récente. Une icône est affichée si l’humeur contient un commentaire. Si une humeur contient un commentaire et que vous cliquez dessus, le commentaire est affiché brièvement en bas de l’écran, via un message de type Toast.

![2020-03-07_15-45-48](2020-03-07_15-45-48.png)
Figure 8: Écran d'historique des humeurs

Si une humeur a été enregistrée avec un commentaire, alors l’icône suivante apparait à droite dans l’historique à la ligne concernant associée.

![2020-03-07_15-46-58](2020-03-07_15-46-58.png)
Figure 9 commentaire

![2020-03-07_15-47-31](2020-03-07_15-47-31.png)
Figure 10: exemple d'humeur avec commentaire

En cliquant sur l’icône, un message Toast apparait en bas de l’écran. Ce Toast aura pour couleur de fond, celle correspondant à l’icône de l’humeur et le message sera le commentaire associé.

![2020-03-07_15-48-05](2020-03-07_15-48-05.png)
Figure 11: affichage du toast message en cliquant sur l'icône commentaire

### 2.5.	 Bonus : partager l’humeur à un ami

Pour partager une humeur particulière à un ami, il suffit de se rendre dans l’historique comme expliquer pré l’icône précédemment. Ensuite, il faut clique sur l’humeur à partager et on sera orienter sur l’écran ci-dessous :

![2020-03-07_15-49-30](2020-03-07_15-49-30.png)
Figure 12: pour aller sélectionner un contact

Il faut cliquer sur ce bouton pour aller sélectionner à qui on souhaite partager l’humeur :

![2020-03-07_15-50-05](2020-03-07_15-50-05.png)

Ensuite l’application nous oriente vers l’interface suivante, puis on sélectionne le contact

![2020-03-07_15-51-32](2020-03-07_15-51-32.png)
Figure 13: liste des contacts

Après avoir sélectionné le contact, l’utilisateur sera dirigé sur cet écran :

![2020-03-07_15-52-09](2020-03-07_15-52-09.png)
Figure 14: pour partager l'humeur

Enfin, on clique sur le bouton « SEND MESSAGE » et le message est envoyé immédiatement au destinataire

## III.	Les technologies utilisées

Pour développer cette application, j’ai utilisé les technologies suivantes :

•	Java et XML comme langages de programmation

•	SQLite pour enregistrer les humeurs dans la mémoire du téléphone

•	La librairie huxylab2 (https://github.com/huxaiphaer/HuxyApp), pour personnaliser les messages Toast

•	La classe ContactsContract pour accéder aux contacts du téléphone, sans oublier la permission : android.permission.READ_CONTACTS

•	Telephony.gsm.SmsManager pour envoyer les sms avec la permission : android.permission.SEND_SMS.
