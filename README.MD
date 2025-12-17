# MasisoKit

**Versiones soportadas:** 1.16.x - 1.21.10  
**Plataformas:** Spigot, Paper  

---

## 📦 Configuración base (`config.yml`)

```yaml
kits:
  bronce:
    delay: 60
    cost: 0.0
    money: 1000.0
    left_click_commands:
      - "[console] say %player_name% ha reclamado el kit bronce"
    items:
      - material: WOODEN_SWORD
        amount: 1
        name: "&l&aESPADA DE NOVATO"
        lore:
          - "&8Kit Bronce"
          - " "
          - "&24 Daño por golpe"
          - "&21.6 Velocidad de ataque"
        enchantments:
          - "SHARPNESS:1"
        attributes:
          - "ATTACK_DAMAGE:10:HAND:ADD_NUMBER"
          - "ATTACK_SPEED:1.7:HAND:ADD_NUMBER"

      - material: LEATHER_HELMET
        amount: 1
        name: "&l&aCASCO DE NOVATO"
        lore:
          - "&8Kit Bronce"
          - " "
          - "&9Armadura: +1"
        enchantments:
          - "PROTECTION_ENVIRONMENTAL:1"
        attributes:
          - "ARMOR:5:HEAD:ADD_NUMBER"

      - material: LEATHER_CHESTPLATE
        amount: 1
        name: "&l&aPECHERA DE NOVATO"
        lore:
          - "&8Kit Bronce"
          - " "
          - "&9Armadura: +3"
        enchantments:
          - "PROTECTION_ENVIRONMENTAL:1"
        attributes:
          - "ARMOR:10:CHEST:ADD_NUMBER"

      - material: LEATHER_LEGGINGS
        amount: 1
        name: "&l&aPANTALON DE NOVATO"
        lore:
          - "&8Kit Bronce"
          - " "
          - "&9Armadura: +2"
        enchantments:
          - "PROTECTION_ENVIRONMENTAL:1"
        attributes:
          - "ARMOR:9:LEGS:ADD_NUMBER"

      - material: LEATHER_BOOTS
        amount: 1
        name: "&l&aBOTAS DE NOVATO"
        lore:
          - "&8Kit Bronce"
          - " "
          - "&9Armadura: +1"
        enchantments:
          - "PROTECTION_ENVIRONMENTAL:1"
        attributes:
          - "ARMOR:6:FEET:ADD_NUMBER"

      - material: BREAD
        amount: 16
        name: "&l&aRACIÓN DE SUPERVIVENCIA"
        lore:
          - "&8Kit Bronce"


=======================================================================

# Kits: es obligatorio para que el plugin funcione
# debe respetar la identación (espacios al inicio de línea)
kits:
  nombreKit:   # nombre del kit, se ejecuta con /masisokit nombreKit (recomendado en minúsculas)
    delay: 60  # tiempo en segundos para volver a reclamar el kit (por defecto 0)
    cost: 0.0  # costo en dinero para reclamar el kit (Vault) (por defecto 0)
    money: 1000.0 # dinero que recibe el jugador (Vault) (por defecto 0)

    # Comandos al hacer click (opcional)
    # [console] ejecuta como consola, [player] como jugador
    left_click_commands:
      - "[console] say %player_name% ha reclamado el kit nombreKit"

    # Items que dará el kit (obligatorio)
    items:
      - material: WOODEN_SWORD   # material obligatorio (en mayúsculas)
        amount: 1                # cantidad (por defecto 1)
        name: "&l&aESPADA DE NOVATO" # nombre custom (opcional)
        lore:                     # lore opcional, recomendado si usas atributos
          - "&8Kit Bronce"
          - " "
          - "&24 Daño por golpe"
          - "&21.6 Velocidad de ataque"
        enchantments:             # lista de encantamientos (opcional)
          - "SHARPNESS:1"
        attributes:               # atributos custom (opcional)
          - "ATTACK_DAMAGE:10:HAND:ADD_NUMBER"
          - "ATTACK_SPEED:1.7:HAND:ADD_NUMBER"

=======================================================================

## 🔧 Notas importantes

- **Materiales:** siempre en mayúsculas.  
  - Lista completa: [Spigot Material API](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)

- **Encantamientos:** formato `ENCANTAMIENTO:NIVEL`.  
  - Lista: [Spigot Enchantment API](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html)

- **Atributos:** formato `TIPO:VALOR:SLOT:OPERACIÓN`.  
  - **Slots disponibles:**
    - `HAND`: Mano principal  
    - `OFF_HAND`: Mano secundaria  
    - `HEAD`: Casco  
    - `CHEST`: Pechera  
    - `LEGS`: Pantalones  
    - `FEET`: Botas  
  - **Operaciones:**
    - `ADD_NUMBER`: suma directa al valor base  
    - `MULTIPLY_SCALAR_1`: multiplica por (1 + amount)  
    - `MULTIPLY_SCALAR_2`: multiplica por amount  
  - Lista de atributos: [Spigot Attribute API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html)

# 🔑 Permisos de MasisoKits

El plugin utiliza un sistema de permisos para controlar el acceso a los kits y a los comandos administrativos.  
A continuación se detallan todos los permisos disponibles:

---

## 📦 Permisos principales

- **`masisokits.use`**  
  - **Default:** `true`  
  - Permite a todos los jugadores usar el comando `/masisokit` y reclamar kits (si tienen permiso específico).

- **`masisokits.reset`**  
  - **Default:** `op`  
  - Permite a los operadores resetear el cooldown de un kit para un jugador.  
  - Comando: `/masisokit reset <jugador> <kit>`

- **`masisokits.reload`**  
  - **Default:** `op`  
  - Permite a los operadores recargar la configuración del plugin.  
  - Comando: `/masisokit reload`

---

## 🎯 Permisos por kit

- **`masisokits.kit.<nombre>`**  
  - **Default:** `false`  
  - Permite reclamar un kit específico.  
  - Ejemplo:  
    - `masisokits.kit.bronce` → habilita el kit **bronce**.  
    - `masisokits.kit.plata` → habilita el kit **plata**.  

- **`masisokits.kit.*`**  
  - **Default:** `false`  
  - Permite reclamar **todos los kits** disponibles.

---

## 📌 Notas

- Los permisos se pueden gestionar con cualquier sistema de permisos (ej: LuckPerms).  
- `masisokits.use` es obligatorio para que el jugador pueda ejecutar `/masisokit`.  
- El acceso a cada kit se controla con `masisokits.kit.<nombre>`.  
- El permiso `masisokits.kit.*` otorga acceso global a todos los kits.  
- Los permisos de administración (`reset` y `reload`) deben reservarse para staff u operadores.

---

## 🛠 Ejemplo en LuckPerms

```yaml
# Grupo Novato: solo acceso al kit bronce
- masisokits.use
- masisokits.kit.bronce

# Grupo VIP: acceso a todos los kits
- masisokits.use
- masisokits.kit.*

# Administrador: acceso completo
- masisokits.use
- masisokits.kit.*
- masisokits.reset
- masisokits.reload
