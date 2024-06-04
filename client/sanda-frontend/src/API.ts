import StoreModel from "./models/StoreModel"

export const getStoreInfo = (token: string, username: string, storeId: number) => {
    let defaultExample: StoreModel = {founderUsername: "mrOwnerMan", storeId: 0, storeName: "TestStore", email: "coolio@gmail.com", address: "B7", rank: 3, phoneNumber: "052-0520520", closingHours: 0, openingHours: 0}
    return defaultExample;
}