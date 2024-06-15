export default interface ProductModel {
    productID: number,
    productName: string,
    productPrice: number,
    productWeight?: number,
    productCatogory?: string,
    description?: string,
    productRank?: number
}