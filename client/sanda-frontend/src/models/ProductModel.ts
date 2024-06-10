export default interface ProductModel {
    id: number,
    name: string,
    storeId: number,
    price: number
    productCatogory?: string
    productDescription?: string
    productRank?: number
}