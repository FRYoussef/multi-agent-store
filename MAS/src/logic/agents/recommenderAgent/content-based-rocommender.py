import sys
import os
import pandas as pd
from typing import List, Dict
import random


def get_user(user_id: int, users: pd.DataFrame) -> Dict:
    dff: pd.DataFrame = users[users['Id'] == user_id]
    l_user = dff.to_dict(orient='records')

    return l_user[0] if len(l_user) > 0 else dict() 


def transform_df(df: pd.DataFrame) -> pd.DataFrame:
    dff = df

    dff['Size'] = dff['Size'].str.split(';')
    dff['Size'] = dff.apply(lambda x: set(x['Size']), axis=1)
    dff['Tags'] = dff['Tags'].str.split(';')
    dff['Tags'] = dff.apply(lambda x: set(x['Tags']), axis=1)

    return dff


def filter_clothing(tags: List[str], df: pd.DataFrame) -> pd.DataFrame:
    dff = df

    for tg in tags:
        dff = dff[dff['Tags'].apply(lambda x: tg in x)]

    return dff


def add_score(df: pd.DataFrame) -> pd.DataFrame:
    dff = df
    dff['Score'] = 0

    for i, row in dff.iterrows():
        dff.loc[i, 'Score'] = row['Sales'] * 0.7 + row['Views'] * 0.3

    dff = dff.sort_values(by=['Score'], ascending=False)
    return dff


def getStringFormat(ids: List[str]) -> str:
    result: str = ""

    for i in ids:
        result += str(i) + ","
    
    # for last comma
    if len(result) > 0:
        result = result[:-1]

    return result


def write_result(text: str) -> None:
    with open("result.txt", "w+") as file:
        file.write(text)


if __name__ == '__main__':
    # remove previous data
    write_result(text="")

    # check arguments
    if len(sys.argv) < 2:
        print('Invalid number of arguments')
        print(f'Help: content-based-recommender.py [list-of-tags]')
        exit(1)

    try:
        tags: List[str] = sys.argv[1:]

        # load clothing
        filename: str = os.path.join('..', 'DB', 'ClothingDB.csv')
        df: pd.DataFrame = pd.read_csv(filename, header=0)
        
        # transform clothing df
        df = transform_df(df=df)

        # filter clothing and calculate score
        df = filter_clothing(tags=tags, df=df)
        df = add_score(df=df)

        # get 10 recomendations. First 3 are always the same, 
        # and the other 2 are random
        ids: List[str] = df['Id'].tolist()
        randoms = ids[3:]
        ids = ids[:3]
        ids = ids + random.choices(population=randoms, k=2)

        write_result(text=getStringFormat(ids=ids))

    except Exception as e:
        write_result(text=f"error\n{e}")
        print(e)
