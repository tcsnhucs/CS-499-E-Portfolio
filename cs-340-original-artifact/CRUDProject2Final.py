# Example Python Code to Insert a Document 

from pymongo import MongoClient

class AnimalShelter(object): 
    """ CRUD operations for Animal collection in MongoDB """ 

    def __init__(self, USER, PASS, HOST, PORT, DB, COL): 
        # Initializing the MongoClient. This helps to access the MongoDB 
        # databases and collections. This is hard-wired to use the aac 
        # database, the animals collection, and the aac user. 
        #
        # Initialize Connection 
        self.client = MongoClient('mongodb://%s:%s@%s:%d' % (USER,PASS,HOST,PORT)) 
        self.database = self.client['%s' % (DB)] 
        self.collection = self.database['%s' % (COL)]
        
    def create(self, data): # Creates a new entry(data) in the database(self)  
        if data is not None: # Only executes function if there is data to execute on
            result = self.collection.insert_one(data)
            return True if result.acknowledged else False
        else:
            raise Exception("Unable to create data, no data to create.")

    def read(self, query): # Reads an entry(query) in the database(self)
        if query is not None: # Only executes function if there is data to execute on
                read_result = list(self.database.animals.find(query, {"_id": False}))
                return read_result
        else:
            raise Exception("Unable to read data, no data to read.")
            return False
            
    def update(self, query, data): # Updates an entry(query) in the database(self) based on the passed through data
        if data is not None: # Only executes function if there is data to execute on
            self.database.animals.update_many(query, data)
        else:
            raise Exception("Unable to update data, no data to update.")
            
    def delete(self, data): # Deletes an entry(data) in the database(self)
        if data is not None: # Only executes function if there is data to execute on
            self.database.animals.delete_many(data)
        else:
            raise Exception("Unable to delete data, no data to delete.")