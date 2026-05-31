
resource "azurerm_virtual_network" "ffmpeg_vnet" {
  name                = "${var.vm_name}-vnet"
  address_space       = ["10.0.0.0/16"]
  location            = var.location
  resource_group_name = var.resource_group_name
}

resource "azurerm_subnet" "ffmpeg_subnet" {
  name                 = "${var.vm_name}-subnet"
  resource_group_name  = var.resource_group_name
  virtual_network_name = azurerm_virtual_network.ffmpeg_vnet.name
  address_prefixes     = ["10.0.1.0/24"]
}

resource "azurerm_public_ip" "ffmpeg_public_ip" {
  name                = "${var.vm_name}-public-ip"
  location            = var.location
  resource_group_name = var.resource_group_name
  allocation_method   = "Static"
}

resource "azurerm_network_security_group" "ffmpeg_nsg" {
  name                = "${var.vm_name}-nsg"
  location            = var.location
  resource_group_name = var.resource_group_name

  security_rule {
    name                       = "SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = "*" # For production, change this to your local machine's IP
    destination_address_prefix = "*"
  }
}

resource "azurerm_network_interface" "ffmpeg_nic" {
  name                = "${var.vm_name}-nic"
  location            = var.location
  resource_group_name = var.resource_group_name

  ip_configuration {
    name                          = "internal-config"
    subnet_id                     = azurerm_subnet.ffmpeg_subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.ffmpeg_public_ip.id
  }
}

resource "azurerm_network_interface_security_group_association" "nic_nsg_connect" {
  network_interface_id      = azurerm_network_interface.ffmpeg_nic.id
  network_security_group_id = azurerm_network_security_group.ffmpeg_nsg.id
}

resource "azurerm_linux_virtual_machine" "ffmpeg_vm" {
  name                            = var.vm_name
  resource_group_name             = var.resource_group_name
  location                        = var.location
  size                            = "Standard_F4s_v2"
  admin_username                  = var.admin_username
  admin_password                  = var.admin_password
  disable_password_authentication = false
  zone                            = "1"

  network_interface_ids = [
    azurerm_network_interface.ffmpeg_nic.id
  ]

  os_disk {
    name                 = "${var.vm_name}_OsDisk_1_614e61fd0d0d41478d4866968e038f47"
    caching              = "ReadWrite"
    storage_account_type = "Premium_LRS"
    disk_size_gb         = 64
  }

  source_image_reference {
    publisher = "canonical"
    offer     = "ubuntu-24_04-lts"
    sku       = "server"
    version   = "latest"
  }

  secure_boot_enabled = true
  vtpm_enabled        = true

  identity {
    type = "SystemAssigned"
  }
}