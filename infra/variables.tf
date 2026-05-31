variable "azure_subscription_id" {
  description = "The target Azure Subscription ID used to provision resources."
  type        = string
}

variable "resource_group_name" {
  description = "The name of the pre-existing Resource Group where the VM should be attached."
  type        = string
}

variable "location" {
  description = "The Azure region for the deployment."
  type        = string
  default     = "centralindia"
}

variable "vm_name" {
  description = "The name designation for your FFmpeg pipeline processing instance."
  type        = string
  default     = "ffmpeg-vm"
}

variable "admin_username" {
  description = "The root configuration username for the VM system."
  type        = string
  default     = "azureuser"
}

variable "admin_password" {
  description = "The administrative login password for system authentication."
  type        = string
  sensitive   = true
}