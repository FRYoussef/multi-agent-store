import os
import dialogflow_v2

from google.api_core.exceptions import InvalidArgument
from random import seed
from random import random

seed(1)

#os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = 'credentials.json'

DIALOGFLOW_PROJECT_ID = 'xenon-height-273419'
DIALOGFLOW_LANGUAGE_CODE = 'en'
SESSION_ID = str(random())



session_client = dialogflow_v2.SessionsClient()
session = session_client.session_path(DIALOGFLOW_PROJECT_ID, SESSION_ID)

all_param = False

while not all_param:
	text_to_be_analyzed = input("User> ")
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
	print('Chatbot> {}\n'.format(
	response.query_result.fulfillment_text))

	if response.query_result.all_required_params_present and response.query_result.intent.display_name == "Buy item":
		all_param = True
