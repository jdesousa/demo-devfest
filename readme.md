# data-integration-product-core

**Why:** Integrate product data to Yucca API in your API to the son of water.

**What:** Connects Yucca API to import data in yours API and the Firebase for integration date stocking.


## Summary

This project is one core for use yucca to eat data in your API. 

For this, you are three service integration :

- Attribute : Data attribute descriptions
- Model : it's compose of attribute id lists and label, use to compose and characterize products
- Product : description of product with one model and list of attributes values

The order of integration is Attribute, Model and last Product

For use :
- First : initialize your api for the models and characteristics with initial date to 01-01-1970, 
and for the product, use the specific method for init to id product file (csv file containing products ids which header)
- Second : the son of water : use a scheduler for integrate the delta every short times (example : 10 minutes) by the order (attributes, models , product)  

## Contributor

Feel free to send a Pull Request.


## Maintainer

[Julien DE SOUSA](julien.desousa@leroymerlin.com)

