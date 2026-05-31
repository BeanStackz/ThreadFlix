terraform {
  required_version = ">= 1.0.0"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 4.0"
    }
  }
}

provider "azurerm" {
  features {}
  subscription_id = var.azure_subscription_id
}

resource "azurerm_resource_group" "ffmpeg_rg" {
  name     = var.resource_group_name
  location = var.location
}

module "compute" {
  source               = "./modules/compute"

  resource_group_name  = azurerm_resource_group.ffmpeg_rg.name
  location             = azurerm_resource_group.ffmpeg_rg.location

  vm_name              = var.vm_name
  admin_username       = var.admin_username
  admin_password       = var.admin_password
}