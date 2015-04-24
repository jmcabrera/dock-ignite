/**
 * 
 */
package io.finin.ignite;

import java.util.Arrays;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

public class IgniteTest {

  public static void main(String[] args) {
    TcpDiscoverySpi spi = new TcpDiscoverySpi();

    TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();

    // Set Multicast group.
    ipFinder.setMulticastGroup("228.10.10.157");

    // Set initial IP addresses.
    // Note that you can optionally specify a port or a port range.
    ipFinder.setAddresses(Arrays.asList("10.0.2.15:49155..49156"));

    spi.setIpFinder(ipFinder);

    IgniteConfiguration cfg = new IgniteConfiguration();

    cfg.setDiscoverySpi(spi);
    cfg.setPeerClassLoadingEnabled(true);

    // Start Ignite node.
    try (Ignite ignite = Ignition.start(cfg)) {
      IgniteCompute compute = ignite.compute();
      for (int i = 0; i < 2; i++) {
        final int j = i;
        compute.broadcast(() -> System.out.println("Hello Node : " + j + " : " + ignite.cluster().localNode().id()));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
