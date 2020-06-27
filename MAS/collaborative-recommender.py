
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sb
from sklearn.cluster import KMeans
from sklearn.metrics import pairwise_distances_argmin_min
import scipy 
import math

customers = pd.read_csv('CustomerDB.csv', sep=',')


views = customers.drop(['Password', 'Preferences', 'Name', 'Gender', 'Purchases'], axis=1)
purchases = customers.drop(['Password', 'Preferences', 'Name', 'Gender', 'Views'], axis=1)
purchases = purchases.dropna()
views = views.dropna()
n_customers = len(customers)


def min_max(array):
    mini = min(array)
    maxi = max(array)
    array_mm = []
    for elem in array:
        if (maxi - mini) != 0:
            result = (elem - mini)/(maxi - mini)
        else:
            result = 0
        array_mm.append(result)
    return array_mm


col_names = ['Id'] + list(range(n_customers))

df = pd.DataFrame(columns = col_names)


for id_user in range(n_customers):
    v_user = []
    p_user = []
    
    v_weight = 0
    p_weight = 0
    
    if id_user in views.index:
        v_user = views['Views'][id_user].split(';')
    
    if id_user in purchases.index:
        p_user = purchases['Purchases'][id_user].split(';')

    #Common purchases
    n_purch_arr = []
    if p_user:
        p_weight = 0.7
        for user_to_compare in range(n_customers):
            n_purch = 0
            if user_to_compare in purchases.index:
                p_tc = purchases['Purchases'][user_to_compare].split(';')
                for item in p_tc:
                    if item in p_user:
                        n_purch += 1
            n_purch_arr.append(n_purch)
    
    #Common views
    n_views_arr = []
    if v_user:
        v_weight = 0.3
        for user_to_compare in range(n_customers):
            n_views = 0
            if user_to_compare in views.index:
                v_tc = views['Views'][user_to_compare].split(';')
                for item in v_tc:
                    if item in v_user:
                        n_views += 1
            n_views_arr.append(n_views)
    
   
    if not p_user: #if there's no purchases, it's 100% based on views
        v_weight = 1
    
   
    
    
    if v_user:
        v_kinship = [i * v_weight for i in n_views_arr]
    else:
        v_kinship = [0]*n_customers
        
    if p_user:
        p_kinship = [i * p_weight for i in n_purch_arr]
        
    else:
        p_kinship = [0]*n_customers
    
    kinship = [x + y for x, y in zip(v_kinship, p_kinship)]
    
    kinship_mm = min_max(kinship)
    
    new_row = [id_user] + kinship_mm
    df.loc[len(df)] = new_row

