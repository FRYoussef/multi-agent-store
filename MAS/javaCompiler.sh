javac \
-Xlint:unchecked \
-cp "lib/jade.jar:." \
--module-path /opt/javafx-sdk-14.0.1/lib \
--add-modules javafx.controls,javafx.fxml \
-d src/classes \
src/Main.java \
src/control/AttachableController.java \
src/control/GuiLauncher.java \
src/control/ItemController.java \
src/control/ItemSelectorController.java \
src/control/LoginController.java \
src/control/RegisterController.java \
src/dataAccess/ClothingDao.java \
src/dataAccess/CsvHandler.java \
src/dataAccess/CustomerDao.java \
src/dataAccess/IDao.java \
src/logic/agents/chatbotAgent/ChatbotAgent.java \
src/logic/agents/chatbotAgent/ChatbotBehaviour.java \
src/logic/agents/chatbotAgent/Intents.java \
src/logic/agents/guiAgent/GuiAgent.java \
src/logic/agents/guiAgent/GuiBehaviour.java \
src/logic/agents/recommenderAgent/pythonArgsAdapter/ColaborativeFilterAdapter.java \
src/logic/agents/recommenderAgent/pythonArgsAdapter/ContentBasedAdapter.java \
src/logic/agents/recommenderAgent/pythonArgsAdapter/IPythonArgs.java \
src/logic/agents/recommenderAgent/RecommenderAgent.java \
src/logic/agents/recommenderAgent/RecommenderBehaviour.java \
src/logic/agents/recommenderAgent/RecommenderMsg.java \
src/logic/service/itemSelectorService/DfaItemSelectorService.java \
src/logic/service/itemSelectorService/ItemSelectorService.java \
src/logic/service/IService.java \
src/logic/service/ItemService.java \
src/logic/service/LoginService.java \
src/logic/service/RegisterService.java \
src/logic/transfer/Clothing.java \
src/logic/transfer/Customer.java \
src/logic/transfer/CustomerResponse.java \
src/logic/transfer/ICsvObjectionable.java

# add views to classes path
cp -r src/views src/classes