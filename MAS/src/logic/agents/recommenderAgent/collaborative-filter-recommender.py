import pandas as pd
import numpy as np
import random
import sys
import os

n_customers = 0
GOOD_RATE = 1.3


def work_out_equals(col, i_user, which):
    n_arr = []
    for user_to_compare in range(n_customers):
        n = 0
        if user_to_compare in which['Id']:
            tc = which[col][user_to_compare].split(';')
            for item in tc:
                if item in i_user:
                    n += 1
        n_arr.append(n)
    return n_arr


#Good recomender = at least kinship with 3 users >= 1.3
# 1.3 means:  >=(2 views, 3 purchases) or  >=(5 views, 0 purchases)
def good_recomender(kinship):
    n_ok = 0
    id_best_kinship = []
    index = 0
    for elem in kinship:
        if elem >= GOOD_RATE and index != user_id:
            n_ok+=1
            id_best_kinship += [index]
        index+=1
    return id_best_kinship, n_ok >=3

def user_kinship(user_id, views, purchases) :

    v_user = []
    p_user = []

    if user_id in views['Id']: #the user has views
        v_user = views['Views'][user_id].split(';')

    if user_id in purchases['Id']: #the user has purchases
        p_user = purchases['Purchases'][user_id].split(';')

    #Common purchases
    n_views_arr = [0] * n_customers
    if v_user:
        n_views_arr = work_out_equals('Views', v_user, views)

    #Common views
    n_purch_arr = [0] * n_customers
    if p_user:
        n_purch_arr = work_out_equals('Purchases', p_user, purchases)

    
    #Weights
    v_weight = 0.3
    p_weight = 0.7
    
    #Calculate kinship with each user 0.3 * common_views + 0.7 * common_purchases
    v_kinship = [i * v_weight for i in n_views_arr]

    p_kinship = [i * p_weight for i in n_purch_arr]
    
    
    kinship = [x + y for x, y in zip(v_kinship, p_kinship)]
    
    return kinship

if __name__ == '__main__':
    
    if len(sys.argv) < 2:
        print('Invalid number of arguments')
        print(f'Help: collaborative-recommender.py user-id')
        exit(1)
    try:
        user_id = int(sys.argv[1])
    except:
        print('user-id must be an integer')
        exit(1)
   
    try:
        filename = '../../../../../DB/CustomerDB.csv'

        customers = pd.read_csv(filename, sep=',')
        
        n_customers = len(customers)
        

        views = customers.drop(['Password', 'Preferences', 'Name', 'Gender', 'Purchases'], axis=1).dropna()
        
        purchases = customers.drop(['Password', 'Preferences', 'Name', 'Gender', 'Views'], axis=1).dropna()
        

        kinship = user_kinship(user_id, views, purchases)
        
        n_users = 5
        result = ""
        ids_best_kinship, is_ok = good_recomender(kinship)
        
        if is_ok: #If it will be a good recommendation
    
            how_many = min(n_users, len(ids_best_kinship)) #number of users with max kinship (3 < how_many < 5)
            sorted(range(len(ids_best_kinship)), key=lambda k: ids_best_kinship[k]) #sort ids by kinship

            user_views = views['Views'][user_id].split(';')
            items = []
            for i in range(how_many): #Get 1 random item of clothing of each user
                user_clothes = ids_best_kinship[i]

                # Get list of the closest user items
                item_list = purchases['Purchases'][user_clothes].split(';')
                if not item_list: #If there's no purchases, search them in views (views always has items bc kinship > 1.3)
                    item_list = views['Views'][user_clothes].split(';')

                len_item_list = len(item_list)
                index_item = random.randrange(0, len_item_list) #Random index item
                item = item_list[index_item] #The item of clothing

                attempt = 2 # Second attempt if already viewed
                while item in user_views:
                    index_item = random.randrange(0, len_item_list)
                    item = item_list[index_item]
                    attempt += 1
                    # If every item is in user views, after "len_item_list*2" attempts, it stops
                    if attempt >= len_item_list*2: break 

                items += [item]

            #It's possible that exist duplicates items -> remove them
            items = list(dict.fromkeys(items))

            #Always 0 < len(item) < 5 
            if len(item) != 0: #When the user viewed every random item checked -> no recommendation
                for s in items:
                    result += s + ","
                result = result[:-1] #Drop last comma
            
        #write result into the file
        print(result)
        f = open("result.txt", "w+")
        f.write(result)
    except:
        #write empty string into the file
        print("No recommendation")
        f = open("result.txt", "w+")
        f.write(result)
        exit(1)
