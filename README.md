Unkindled makes furnaces no longer smelt items automatically.
Instead, players need to use a flint and steel (any tool in `c:tools/igniter`)
or a fire charge to ignite the furnace (while sneaking).
Furnaces will continue to burn as long as there is fuel in them.

Campfires are also affected. Newly placed campfires will be unlit.

**Note: This mod does not provide any early game fire starters!**

## Automation

Unkindled adds extra behavior to the dispenser, allowing it to ignite furnaces using a flint and steel.

## Configuration

The mod is controlled using tags. One for items and one for blocks.
Replace or add to them according to your needs.

#### Needs Igniting (Block)

`unkindled:needs_igniting`  
Blocks in this tag require to be manually ignited using a flint and steel or a fire charge.
Includes all vanilla furnaces and campfires by default.

Any furnace or campfire block added to this tag, that inherits from the vanilla furnace or campfire respectively, should work out of the box.

#### Self-Igniting Fuel (Item)

`unkindled:self_igniting_fuel`  
Items in this tag bypass the need to manually ignite a furnace.
Includes the bucket of lava and blaze rod by default.

## Sidedness

While this is technically not required on the client, slight visual errors will occur.  
If possible, Unkindled should be installed on both server and client.