import os
import dialogflow_v2
import sys

from google.api_core.exceptions import InvalidArgument

#os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = 'credentials.json'

DIALOGFLOW_PROJECT_ID = 'xenon-height-273419'
DIALOGFLOW_LANGUAGE_CODE = 'en'


if len(sys.argv) < 3:
        print('Invalid number of arguments')
        print(f'Help: chatbot.py id-user msg')
        exit(1)

SESSION_ID = sys.argv[1]



session_client = dialogflow_v2.SessionsClient()
session = session_client.session_path(DIALOGFLOW_PROJECT_ID, SESSION_ID)

text_to_be_analyzed = ""

i = 2
while i < len(sys.argv):
	text_to_be_analyzed = text_to_be_analyzed + " " + sys.argv[i]
	i += 1

text_input = dialogflow_v2.types.TextInput(text=text_to_be_analyzed, language_code=DIALOGFLOW_LANGUAGE_CODE)
query_input = dialogflow_v2.types.QueryInput(text=text_input)
try:
	response = session_client.detect_intent(session=session, query_input=query_input)

except InvalidArgument:
	raise
all_required = response.query_result.all_required_params_present
intent = format(response.query_result.intent.display_name)
msg = format(response.query_result.fulfillment_text)

params_s = ""
params_list = response.query_result.parameters.ListFields()
if(len(params_list) > 0):
	gender = params_list[0][1]['Gender'].string_value
	color = params_list[0][1]['Color'].string_value
	item = params_list[0][1]['Clothes'].string_value
	params_s = gender + "," + color + "," + item
f = open("result.txt", "w")
if (params_s != ""):
	f.write(params_s + "\n" + str(all_required) + "\n" + msg + "\n" + intent)
else:
	f.write("no_params" + "\n" + str(all_required) + "\n" + msg + "\n" + intent)

f.close()

