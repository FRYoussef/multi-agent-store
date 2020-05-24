import os
import dialogflow_v2
import sys

from google.api_core.exceptions import InvalidArgument

#os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = 'credentials.json'

DIALOGFLOW_PROJECT_ID = 'xenon-height-273419'
DIALOGFLOW_LANGUAGE_CODE = 'en'
SESSION_ID = sys.argv[1]



session_client = dialogflow_v2.SessionsClient()
session = session_client.session_path(DIALOGFLOW_PROJECT_ID, SESSION_ID)

text_to_be_analyzed = ""

i = 2
while i < len(sys.argv):
	text_to_be_analyzed = text_to_be_analyzed + " " + sys.argv[i]
	i += 1

print(text_to_be_analyzed)

text_input = dialogflow_v2.types.TextInput(text=text_to_be_analyzed, language_code=DIALOGFLOW_LANGUAGE_CODE)
query_input = dialogflow_v2.types.QueryInput(text=text_input)
try:
	response = session_client.detect_intent(session=session, query_input=query_input)

except InvalidArgument:
	raise

#print('Query text: {}'.format(response.query_result.query_text))
#print('Detected intent: {} (confidence: {})\n'.format(
#response.query_result.intent.display_name,
#response.query_result.intent_detection_confidence))
#print('Chatbot> {}\n'.format(response.query_result.fulfillment_text))
f = open("result.txt", "w")
if response.query_result.all_required_params_present and response.query_result.intent.display_name == "Buy item":
	f.write("END\n" + format(response.query_result.fulfillment_text))
else:
    f.write("CONTINUE\n" + format(response.query_result.fulfillment_text))
f.close()

