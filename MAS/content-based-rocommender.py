import sys
import os
import pandas as pd
from typing import List, Dict


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


def filter_clothing(user: Dict, tags: List[str], df: pd.DataFrame) -> pd.DataFrame:
    dff = df

    # by gender
    dff = dff[dff['Tags'].apply(lambda x: user['Gender'] in x)]

    # by tags
    for tg in tags:
        dff = dff[dff['Tags'].apply(lambda x: tg in x)]

    return dff


def add_score(df: pd.DataFrame) -> pd.DataFrame:
    dff = df
    dff['Score'] = 0

    for i, row in dff.iterrows():
        dff.loc[i, 'Score'] = row['Sales'] * 0.6 + row['Views'] * 0.4

    dff = dff.sort_values(by=['Score'], ascending=False)
    return dff


if __name__ == '__main__':
    # chack arguments
    if len(sys.argv) < 2:
        print('Invalid number of arguments')
        print(f'Help: content-based-recommender.py user-id [list-of-tags]')
        exit(1)

    user_id: int = sys.argv[1]
    tags: List[str] = sys.argv[2:] if len(sys.argv) > 2 else list()

    # load clothing
    filename: str = os.path.join('DB', 'ClothingDB.csv')
    df: pd.DataFrame = pd.read_csv(filename, header=0)

    # load users
    filename: str = os.path.join('DB', 'UserDB.csv')
    users: pd.DataFrame = pd.read_csv(filename, header=0)
    users['Id'].apply(lambda x: int(x))

    user: Dict = get_user(user_id=int(user_id), users=users)
    
    # transform clothing df
    df = transform_df(df=df)

    # filter clothing and calculate score
    df = filter_clothing(user=user, tags=tags, df=df)
    df = add_score(df=df)

    # 5 recommendations
    # df.head()['Id'].tolist()
    print(df.head())
