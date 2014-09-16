/*global cnrutils,search,status,args,_,logger,jsonUtils,model,requestbody */
function main() {
  "use strict";
  var json = jsonUtils.toObject(requestbody.content),
    applicationSource = search.findNode(json.applicationSourceId),
    userId = applicationSource.getProperties()["jconon_application:user"],
    j = 0,
    child;

  applicationSource.getProperties()["jconon_application:stato_domanda"] = "P";
  applicationSource.getProperties()["jconon_application:data_domanda"] = null;
  applicationSource.save();
  applicationSource.removePermission("Consumer", userId);
  applicationSource.setPermission("Contributor", userId);
  for (j = 0; j < applicationSource.children.length; j++) {
    child = applicationSource.children[j];
    if (String(child.getTypeShort()) !==  "jconon_attachment:application" &&
        String(child.getOwner()) !== userId) {
      child.setOwner(userId);
    }
  }
  model.esito = true;
}
main();